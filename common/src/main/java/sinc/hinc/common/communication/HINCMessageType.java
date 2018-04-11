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

    //TODO remove unused

    SYN_REQUEST,
    SYN_REPLY,
    // unicast:local->global, local manager register it self
    local_register,
    // unicast: Client->local, query information from local regarding to SD Gateway or NVF
    QUERY_IOT_UNIT,
    QUERY_IOT_PROVIDERS,
    QUERY_MICRO_SERVICE_LOCAL,
    QUERY_NFV_LOCAL,
    // unicast: Client->global, query information from global (which include relationship)
    QUERY_INFORMATION_GLOBAL,
    // unicast: Client->local, send a control command to local
    //        RPC_CONTROL_LOCAL,
    // unicast: Client->global, send a control command to global
    // unicast/broadcast: Client--> local: subscribe the changes in the gateway
    SUBSCRIBE_SDGATEWAY_LOCAL,
    SUBSCRIBE_SDGATEWAY_LOCAL_SET_PARAM,
    // unicast: local/global --> client: send back the response
    UPDATE_INFORMATION_SINGLEIOTUNIT,
    UPDATE_INFORMATION_MICRO_SERVICE,
    UPDATE_INFORMATION_IOTPROVIDER,

    // HINC Local update info base: global --> client
    UPDATE_INFO_BASE,

    // Provider send an update of IoT Unit to HINC Local
    PROVIDER_UPDATE_IOT_UNIT,

    // MISC
    MISC,

}
