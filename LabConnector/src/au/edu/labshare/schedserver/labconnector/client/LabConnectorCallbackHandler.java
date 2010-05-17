
/**
 * LabConnectorCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.1  Built on : Oct 19, 2009 (10:59:00 EDT)
 */

    package au.edu.labshare.labconnector;

    /**
     *  LabConnectorCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class LabConnectorCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public LabConnectorCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public LabConnectorCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for getExperimentResults method
            * override this method for handling normal response from getExperimentResults operation
            */
           public void receiveResultgetExperimentResults(
                    au.edu.labshare.labconnector.LabConnectorStub.GetExperimentResultsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getExperimentResults operation
           */
            public void receiveErrorgetExperimentResults(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deleteSavedUserExperimentInput method
            * override this method for handling normal response from deleteSavedUserExperimentInput operation
            */
           public void receiveResultdeleteSavedUserExperimentInput(
                    au.edu.labshare.labconnector.LabConnectorStub.DeleteSavedUserExperimentInputResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deleteSavedUserExperimentInput operation
           */
            public void receiveErrordeleteSavedUserExperimentInput(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getSavedUserExperimentInput method
            * override this method for handling normal response from getSavedUserExperimentInput operation
            */
           public void receiveResultgetSavedUserExperimentInput(
                    au.edu.labshare.labconnector.LabConnectorStub.GetSavedUserExperimentInputResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getSavedUserExperimentInput operation
           */
            public void receiveErrorgetSavedUserExperimentInput(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for setUserPermissions method
            * override this method for handling normal response from setUserPermissions operation
            */
           public void receiveResultsetUserPermissions(
                    au.edu.labshare.labconnector.LabConnectorStub.SetUserPermissionsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from setUserPermissions operation
           */
            public void receiveErrorsetUserPermissions(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getLabStatus method
            * override this method for handling normal response from getLabStatus operation
            */
           public void receiveResultgetLabStatus(
                    au.edu.labshare.labconnector.LabConnectorStub.GetLabStatusResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getLabStatus operation
           */
            public void receiveErrorgetLabStatus(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getExperimentID method
            * override this method for handling normal response from getExperimentID operation
            */
           public void receiveResultgetExperimentID(
                    au.edu.labshare.labconnector.LabConnectorStub.GetExperimentIDResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getExperimentID operation
           */
            public void receiveErrorgetExperimentID(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for saveExperimentResults method
            * override this method for handling normal response from saveExperimentResults operation
            */
           public void receiveResultsaveExperimentResults(
                    au.edu.labshare.labconnector.LabConnectorStub.SaveExperimentResultsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from saveExperimentResults operation
           */
            public void receiveErrorsaveExperimentResults(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for saveUserExperimentInput method
            * override this method for handling normal response from saveUserExperimentInput operation
            */
           public void receiveResultsaveUserExperimentInput(
                    au.edu.labshare.labconnector.LabConnectorStub.SaveUserExperimentInputResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from saveUserExperimentInput operation
           */
            public void receiveErrorsaveUserExperimentInput(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getExperimentSpecs method
            * override this method for handling normal response from getExperimentSpecs operation
            */
           public void receiveResultgetExperimentSpecs(
                    au.edu.labshare.labconnector.LabConnectorStub.GetExperimentSpecsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getExperimentSpecs operation
           */
            public void receiveErrorgetExperimentSpecs(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getInteractiveExperimentSession method
            * override this method for handling normal response from getInteractiveExperimentSession operation
            */
           public void receiveResultgetInteractiveExperimentSession(
                    au.edu.labshare.labconnector.LabConnectorStub.GetInteractiveExperimentSessionResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getInteractiveExperimentSession operation
           */
            public void receiveErrorgetInteractiveExperimentSession(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getMaintenanceTime method
            * override this method for handling normal response from getMaintenanceTime operation
            */
           public void receiveResultgetMaintenanceTime(
                    au.edu.labshare.labconnector.LabConnectorStub.GetMaintenanceTimeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getMaintenanceTime operation
           */
            public void receiveErrorgetMaintenanceTime(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for cancelBookingTime method
            * override this method for handling normal response from cancelBookingTime operation
            */
           public void receiveResultcancelBookingTime(
                    au.edu.labshare.labconnector.LabConnectorStub.CancelBookingTimeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from cancelBookingTime operation
           */
            public void receiveErrorcancelBookingTime(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getLabInfo method
            * override this method for handling normal response from getLabInfo operation
            */
           public void receiveResultgetLabInfo(
                    au.edu.labshare.labconnector.LabConnectorStub.GetLabInfoResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getLabInfo operation
           */
            public void receiveErrorgetLabInfo(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for setMaintenanceTime method
            * override this method for handling normal response from setMaintenanceTime operation
            */
           public void receiveResultsetMaintenanceTime(
                    au.edu.labshare.labconnector.LabConnectorStub.SetMaintenanceTimeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from setMaintenanceTime operation
           */
            public void receiveErrorsetMaintenanceTime(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getLabID method
            * override this method for handling normal response from getLabID operation
            */
           public void receiveResultgetLabID(
                    au.edu.labshare.labconnector.LabConnectorStub.GetLabIDResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getLabID operation
           */
            public void receiveErrorgetLabID(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for submitExperiment method
            * override this method for handling normal response from submitExperiment operation
            */
           public void receiveResultsubmitExperiment(
                    au.edu.labshare.labconnector.LabConnectorStub.SubmitExperimentResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from submitExperiment operation
           */
            public void receiveErrorsubmitExperiment(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for scheduleBookingTime method
            * override this method for handling normal response from scheduleBookingTime operation
            */
           public void receiveResultscheduleBookingTime(
                    au.edu.labshare.labconnector.LabConnectorStub.ScheduleBookingTimeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from scheduleBookingTime operation
           */
            public void receiveErrorscheduleBookingTime(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getToken method
            * override this method for handling normal response from getToken operation
            */
           public void receiveResultgetToken(
                    au.edu.labshare.labconnector.LabConnectorStub.GetTokenResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getToken operation
           */
            public void receiveErrorgetToken(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for cancelMaintenanceTime method
            * override this method for handling normal response from cancelMaintenanceTime operation
            */
           public void receiveResultcancelMaintenanceTime(
                    au.edu.labshare.labconnector.LabConnectorStub.CancelMaintenanceTimeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from cancelMaintenanceTime operation
           */
            public void receiveErrorcancelMaintenanceTime(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for releaseSlave method
            * override this method for handling normal response from releaseSlave operation
            */
           public void receiveResultreleaseSlave(
                    au.edu.labshare.labconnector.LabConnectorStub.ReleaseSlaveResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from releaseSlave operation
           */
            public void receiveErrorreleaseSlave(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getExperimentType method
            * override this method for handling normal response from getExperimentType operation
            */
           public void receiveResultgetExperimentType(
                    au.edu.labshare.labconnector.LabConnectorStub.GetExperimentTypeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getExperimentType operation
           */
            public void receiveErrorgetExperimentType(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getExperimentStatus method
            * override this method for handling normal response from getExperimentStatus operation
            */
           public void receiveResultgetExperimentStatus(
                    au.edu.labshare.labconnector.LabConnectorStub.GetExperimentStatusResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getExperimentStatus operation
           */
            public void receiveErrorgetExperimentStatus(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for releaseExperiment method
            * override this method for handling normal response from releaseExperiment operation
            */
           public void receiveResultreleaseExperiment(
                    au.edu.labshare.labconnector.LabConnectorStub.ReleaseExperimentResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from releaseExperiment operation
           */
            public void receiveErrorreleaseExperiment(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getUserPermissions method
            * override this method for handling normal response from getUserPermissions operation
            */
           public void receiveResultgetUserPermissions(
                    au.edu.labshare.labconnector.LabConnectorStub.GetUserPermissionsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getUserPermissions operation
           */
            public void receiveErrorgetUserPermissions(java.lang.Exception e) {
            }
                


    }
    