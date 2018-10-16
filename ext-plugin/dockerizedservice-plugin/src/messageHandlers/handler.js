class Handler{
    constructor(){
        this.handlers = [];
    }

    use(msgType ,handle){
        this.handlers.push({
            msgType,
            handle,
        });
    }

    handle(msg){
        for(let i=0;i<this.handlers.length;i++){
            if(msg.msgType !== this.handlers[i].msgType) continue;
            return this.handlers[i].handle(msg);
        }
        console.log(`no handler found for ${msg.msgType}`);
        return new Promise((resolve, reject) => resolve(null))
    }
}

let handler = new Handler();
// register handlers
handler.use('QUERY_RESOURCES', require('./handleQueryResources'));
handler.use('QUERY_PROVIDER', require('./handleQueryProvider'));
//handler.use('REGISTER_ADAPTOR', require('./handleQueryProvider'));
handler.use('CONTROL', require('./handleSendControl'));
handler.use('PROVISION', require('./handleProvision'));
handler.use('DELETE', require('./handleDelete'));
handler.use('CONFIGURE', require('./handleConfigure'));

module.exports = handler
