import stompPathsConfig from './stomp/stompPathsConfig';

class SessionsApiQueryManager {

    constructor(stompQueryManager) { 
        this.stompQueryManager = stompQueryManager;
    }

    /**
     * Get sessions initial status
     */
    getSessionsStatus = () => {

        let params = this.buildParams(stompPathsConfig.paths.getSessionsStatusQueryDestinationPath());
        return this.stompQueryManager.send(params);
    };

    /**
     *
     * @param sessionId
     */
    startSession = (sessionId) => {


        return this.manageSession(sessionId, "start");
    };

    /**
     *
     * @param sessionId
     */
    pauseSession = (sessionId) => {

        return this.manageSession(sessionId, "pause");
    };

    /**
     *
     * @param sessionId
     */
    resumeSession = (sessionId) => {


        return this.manageSession(sessionId, "start");
    };

    /**
     *
     * @param sessionId
     */
    stopSession = (sessionId) => {


        return this.manageSession(sessionId, "stop");
    };

    /**
     *
     * @param sessionId
     */
    restartSession = (sessionId) => {


        return this.stopSession(sessionId).then(() => this.startSession(sessionId));
    };

    /**
     *
     * @param destinationPath
     * @param command
     */
    buildParams = (destinationPath, command = {}) => {

        return {
            destinationPath,
            subscriptionPath: stompPathsConfig.paths.getQuerySubscriptionPath(destinationPath),
            payload: command
        }
    };

    /**
     *
     * @param sessionId
     * @param command
     */
    manageSession = (sessionId, command) => {

        let params = this.buildParams(stompPathsConfig.paths.getSessionQueryDestinationPath(sessionId), {command});
        return this.stompQueryManager.send(params);
    }
}

export default SessionsApiQueryManager;
