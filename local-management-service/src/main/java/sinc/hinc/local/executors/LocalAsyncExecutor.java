package sinc.hinc.local.executors;

import org.slf4j.Logger;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.communication.payloads.ControlResult;
import sinc.hinc.local.ProcessStorage;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;

import java.io.IOException;

public class LocalAsyncExecutor implements ExecutorsInterface {

    static Logger logger = HincConfiguration.getLogger();

    @Override
    public ControlResult execute(ControlPoint controlPoint) {
        try {
            ProcessStorage processStorage = ProcessStorage.getInstance();

            String processKey = controlPoint.getIotUnitID() + "$ " + controlPoint.getReference();
            Process process = processStorage.getProcess(processKey);

            if(controlPoint.getControlType() == ControlPoint.ControlType.EXECUTE_LOCAL_PROCESS){

                logger.debug("execute local process with async executor");

                if(process!=null) {
                    logger.debug("destroy preexisting process " + processKey);
                    process.destroy();
                    processStorage.removeProcess(processKey);
                }

                ProcessBuilder processBuilder = new ProcessBuilder();

                String parameters = controlPoint.getParameters();
                if (parameters != null) {

                    parameters = parameters.replaceAll("(?s)\\<.*?\\>", "");
                    parameters = parameters.replaceAll("(?s)\\[.*?\\]", "").trim();
                }


                if (parameters == null || parameters.isEmpty()) {
                    processBuilder.command(controlPoint.getReference());
                } else {
                    processBuilder.command(controlPoint.getReference(), parameters);
                }

                //TODO remove inherited IO
                processBuilder.inheritIO();

                logger.debug("start process " + processKey + " " + parameters);
                process = processBuilder.start();
                processStorage.putProcess(processKey, process);

                return new ControlResult(ControlResult.CONTROL_RESULT.SUCCESS, 0, "started: " + processKey);
            }else if(controlPoint.getControlType() == ControlPoint.ControlType.DESTROY_LOCAL_PROCESS) {
                logger.debug("destroy local process with async executor " + processKey);
                if(process!=null){
                    process.destroy();
                    processStorage.removeProcess(processKey);
                }
                return new ControlResult(ControlResult.CONTROL_RESULT.SUCCESS, 0, "stopped: " + processKey);
            }

            return new ControlResult(ControlResult.CONTROL_RESULT.EXECUTOR_ERROR, 0, "Unknown ControlType ");


        } catch (IOException ex) {
            ex.printStackTrace();
            return new ControlResult(ControlResult.CONTROL_RESULT.EXECUTOR_ERROR, 0, ex.getMessage());
        }
    }
}
/* input { http_poller{ urls => { datasource => { url => "http://datasource:80" method => get} } schedule => {"every" => "5s"} } } output { rabbitmq{ host => "rabbitmq" exchange_type => "direct" exchange => "push_rabbitmq" } }
 */