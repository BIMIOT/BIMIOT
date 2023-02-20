package com.iba.iot.datasimulator.session.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iba.iot.datasimulator.common.model.ErrorResponse;
import com.iba.iot.datasimulator.common.util.CollectionUtil;
import com.iba.iot.datasimulator.session.model.Session;
import com.iba.iot.datasimulator.session.model.SessionCreateUpdateRequest;
import com.iba.iot.datasimulator.session.model.SessionViews;
import com.iba.iot.datasimulator.session.service.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.iba.iot.datasimulator.session.model.active.command.CommandResult;
import com.iba.iot.datasimulator.session.service.active.manager.ActiveSessionManager;
import com.iba.iot.datasimulator.session.model.active.message.ActiveSessionsStatusCommandResultMessage;
import com.iba.iot.datasimulator.session.model.active.command.ActiveSessionManagementCommand;
import com.iba.iot.datasimulator.session.model.active.message.ActiveSessionManagementCommandResultMessage;
import com.iba.iot.datasimulator.session.model.active.command.SessionManagementCommand;
import com.iba.iot.datasimulator.definition.model.Dataset;
import com.iba.iot.datasimulator.common.model.schema.Schema;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import org.xmlpull.v1.XmlPullParserException;
import io.minio.errors.*;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import com.iba.iot.datasimulator.definition.controller.DatasetController;
import com.iba.iot.datasimulator.definition.controller.DataDefinitionController;
import com.iba.iot.datasimulator.definition.model.DataDefinitionCreateUpdateRequest;
import com.iba.iot.datasimulator.definition.model.DataDefinition;
import com.iba.iot.datasimulator.session.model.active.timer.DatasetTimer;
import com.iba.iot.datasimulator.session.model.active.generator.SchemaBasedGenerator;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Collection;

@RestController
@RequestMapping(value = "/v1/sessions")
public class SessionRestController {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(SessionRestController.class);

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private ActiveSessionManager activeSessionManager;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private DatasetController datasetController;

    @Autowired
    private DataDefinitionController dataDefinitionController;

    @RequestMapping(method = RequestMethod.POST)
    public Session create(@RequestBody @Valid @NotNull SessionCreateUpdateRequest sessionCreateUpdateRequest) {

        return sessionManager.create(sessionCreateUpdateRequest);
    }

    @RequestMapping(method = RequestMethod.GET)
    @JsonView(SessionViews.Short.class)
    public Collection<Session> get() {

        return sessionManager.get();
    }

    // --- Methods to connect to BIMIOT app --- //

    /* Annotation */
    @RequestMapping(value = "/create/{name}", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    public Session createStepByStep(HttpServletRequest request, @PathVariable("name") @NotNull String name) throws IOException, InvalidKeyException, NoSuchAlgorithmException,
            XmlPullParserException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException,
            InsufficientDataException, ErrorResponseException, RegionConflictException, InvalidPortException, InvalidEndpointException, InvalidObjectPrefixException, FileUploadException {

        // Upload dataset
        Dataset dataset = datasetController.upload(request);

        // Create definition
        Schema schema = datasetController.deriveSchema(dataset.getId().toString());
        DataDefinitionCreateUpdateRequest ddCreateUpdate = new DataDefinitionCreateUpdateRequest();
        ddCreateUpdate.setName(name);
        ddCreateUpdate.setDatasetId(dataset.getId().toString());
        ddCreateUpdate.setSchema(schema);
        DataDefinition dataDefinition = dataDefinitionController.create(ddCreateUpdate);

        // Create session
        SessionCreateUpdateRequest ssCreateUpdate = new SessionCreateUpdateRequest();
        ssCreateUpdate.setName(name);
        ssCreateUpdate.setDataDefinitionId(dataDefinition.getId().toString());
        ssCreateUpdate.setIsReplayLooped(false);
        ssCreateUpdate.setTargetSystemId("63efadc4d193183e33294ea1");

        DatasetTimer timer = new DatasetTimer();
        timer.setDatePosition("timestamp");
        ssCreateUpdate.setTimer(timer);

        SchemaBasedGenerator generator = new SchemaBasedGenerator();
        Schema sessionSchema = dataDefinitionController.populateSchemaDefaultRules(dataDefinition.getId().toString());
        generator.setSchema(sessionSchema);
        ssCreateUpdate.setGenerator(generator);

        return sessionManager.create(ssCreateUpdate);
    }


    @RequestMapping(value = "/getStatus/{sessionId}", method = RequestMethod.GET)
    public ActiveSessionsStatusCommandResultMessage getStatuses(@PathVariable("sessionId") @NotNull String sessionId) throws Exception {

        logger.debug("Get all active sessions statuses.");

        ActiveSessionsStatusCommandResultMessage message =
                new ActiveSessionsStatusCommandResultMessage(activeSessionManager.getSessionStatuses(), CommandResult.SUCCESS);
        return message;
    }

    @RequestMapping(value = "/start/{sessionId}", method = RequestMethod.PUT)
    public ActiveSessionManagementCommandResultMessage start(@PathVariable("sessionId") @NotNull String sessionId) throws Exception {

        ActiveSessionManagementCommand command = new ActiveSessionManagementCommand(SessionManagementCommand.START);
        logger.debug("Performing command {} for session {}.", command.getCommand(), sessionId);
        ActiveSessionManagementCommandResultMessage message = activeSessionManager.manage(sessionId, command);

        return message;
    }

    @RequestMapping(value = "/stop/{sessionId}", method = RequestMethod.PUT)
    public ActiveSessionManagementCommandResultMessage stop(@PathVariable("sessionId") @NotNull String sessionId) throws Exception {

        ActiveSessionManagementCommand command = new ActiveSessionManagementCommand(SessionManagementCommand.STOP);
        logger.debug("Performing command {} for session {}.", command.getCommand(), sessionId);
        ActiveSessionManagementCommandResultMessage message = activeSessionManager.manage(sessionId, command);

        return message;
    }

    @RequestMapping(value = "/manage/{sessionId}", method = RequestMethod.PUT)
    public ActiveSessionManagementCommandResultMessage manage(@PathVariable("sessionId") @NotNull String sessionId,
                                                              @RequestBody @Valid @NotNull ActiveSessionManagementCommand command) throws Exception {

        logger.debug("Performing command {} for session {}.", command.getCommand(), sessionId);
        ActiveSessionManagementCommandResultMessage message = activeSessionManager.manage(sessionId, command);

        return message;
    }

    // --- //

    @RequestMapping(value = "/{sessionId}", method = RequestMethod.GET)
    public Session get(@PathVariable("sessionId") @NotNull String sessionId) {

        return sessionManager.get(sessionId);
    }

    @RequestMapping(value = "/{sessionId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("sessionId") @NotNull String sessionId) {
        sessionManager.remove(sessionId);
    }

    @RequestMapping(value = "/{sessionId}", method = RequestMethod.PUT)
    public Session update(@PathVariable("sessionId") @NotNull String sessionId,
                          @RequestBody @Valid @NotNull SessionCreateUpdateRequest sessionCreateUpdateRequest) {

       return sessionManager.update(sessionId, sessionCreateUpdateRequest);
    }

    @RequestMapping(value = "/{sessionId}/export", method = RequestMethod.GET)
    public void export(@PathVariable("sessionId") @NotNull String sessionId,
                       HttpServletResponse response) throws IOException {


        logger.debug(">>> Exporting session: {}", sessionId);
        Session session = sessionManager.get(sessionId);
        if (session != null) {
            processSessionExport(response, session);
        } else {

            logger.error(">>> Cannot export session by id {}", sessionId);
            throw new IllegalArgumentException("Non existing session id error.");
        }
    }

    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public void importSession(@RequestBody @Valid @NotNull Session session) throws IOException {

        sessionManager.importSession(session);
    }


    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(Exception exception) {
        logger.warn(">>> Wrong request params are provided: {}", exception.getMessage());
        return new ErrorResponse(exception.getMessage());
    }

    /**
     *
     * @param response
     * @param session
     * @throws IOException
     */
    private void processSessionExport(HttpServletResponse response, Session session) throws IOException {

        String sessionJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(session);
        response.setContentType("application/json");
        String fileName = session.getName().toLowerCase() + ".json";
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        response.getOutputStream().print(sessionJson);
        response.flushBuffer();
    }
}
