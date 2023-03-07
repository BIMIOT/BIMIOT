import stompSubscriptionManager from './stomp/stompSubscriptionManager';
import stompPathsConfig from './stomp/stompPathsConfig';

class SessionsApiSubscriptionManager {


    constructor(subscriptionManager) { 
        this.subscriptionManager = subscriptionManager;
    }

    /**
     *
     * @param consumer
     */
    subscribeToAllSessions = (consumer) => {


        let params = {subscriptionPath: stompPathsConfig.paths.getSessionsSubscriptionPath(), consumer};
        return this.subscriptionManager.subscribe(params);
    };

    /**
     *
     * @param sessiondId
     * @param consumer
     */
    subscribeToSession = (sessiondId, consumer) => {

        let subscriptionPath = stompPathsConfig.paths.getSessionSubscriptionPath(sessiondId);
        let params = {subscriptionPath, consumer};
        return this.subscriptionManager.subscribe(params);
    };

    /**
     *
     * @param sessionId
     * @param consumer
     */
    subscribeToSessionPayload = (sessionId, consumer) => {


        let subscriptionPath = stompPathsConfig.paths.getSessionPayloadSubscriptionPath(sessionId);
        let params = {subscriptionPath, consumer};
        return this.subscriptionManager.subscribe(params);
    };

    /**
     *
     * @param sessionId
     * @param consumer
     */
    subscribeToSessionErrors = (sessionId, consumer) => {


        let subscriptionPath = stompPathsConfig.paths.getSessionErrorsSubscriptionPath(sessionId);
        let params = {subscriptionPath, consumer};
        return this.subscriptionManager.subscribe(params);
    };

    /**
     *
     * @param sessionId
     * @param consumer
     */
    subscribeToSessionAnalytics = (sessionId, consumer) => {


        let subscriptionPath = stompPathsConfig.paths.getSessionAnalyticsSubscriptionPath(sessionId);
        let params = {subscriptionPath, consumer};
        return this.subscriptionManager.subscribe(params);
    };

    /**
     *
     * @param consumer
     */
    subscribeToErrors = (consumer) => {


        let params = {subscriptionPath: stompPathsConfig.paths.getErrorsSubscriptionPath(), consumer};
        return this.subscriptionManager.subscribe(params);
    };

}

export default SessionsApiSubscriptionManager;
