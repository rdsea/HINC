/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.common.communication;

/**
 *
 * @author hungld
 */
public enum HINCMessageType {
    QUERY_RESOURCES,
    UPDATE_RESOURCES,

    QUERY_PROVIDER,
    UPDATE_PROVIDER,

    CONTROL,
    CONTROL_RESULT,

    FETCH_RESOURCES,
    DELIVER_RESOURCES,

    FETCH_PROVIDERS,
    DELIVER_PROVIDERS,
    REGISTER_LMS,

    REGISTER_ADAPTOR,
    DEREGISTER_ADAPTOR,

    PROVISION,
    PROVISION_RESULT,

    DELETE,
    DELETE_RESULT,

}
