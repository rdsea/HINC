package sinc.hinc.local.messageHandlers.control.invokers;

import com.squareup.okhttp.*;
import sinc.hinc.communication.payloads.ControlResult;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;

import java.io.IOException;

public class PostInvoker implements Invoker {
    public static final MediaType JSON = MediaType.parse("application/json; charset=urf-8");

    @Override
    public ControlResult invoke(ControlPoint controlPoint) {

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, controlPoint.getParameters());

        Request request = new Request.Builder()
                .url(controlPoint.getReference())
                .post(body)
                .build();

        ControlResult result = new ControlResult();
        Response response = null;
        try {
            response = client.newCall(request).execute();

            if(response.isSuccessful()){
                result.setResult(ControlResult.CONTROL_RESULT.SUCCESS);
            }else{
                result.setResult(ControlResult.CONTROL_RESULT.COMMAND_EXIT_ERROR);
            }

            result.setOutput(response.body().string());

        } catch (IOException e) {
            result.setResult(ControlResult.CONTROL_RESULT.COMMAND_EXIT_ERROR);
            e.printStackTrace();
        }

        return result;
    }
}
