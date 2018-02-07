package sinc.hinc.local.messageHandlers.control.invokers;

import sinc.hinc.communication.payloads.ControlResult;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;

import java.io.IOException;

public interface Invoker {
    public ControlResult invoke(ControlPoint controlPoint);
}
