package sinc.hinc.local.messageHandlers.control.invokers;

import sinc.hinc.communication.payloads.ControlResult;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;

public interface IInvoker {
    public ControlResult invoke(ControlPoint controlPoint)
}
