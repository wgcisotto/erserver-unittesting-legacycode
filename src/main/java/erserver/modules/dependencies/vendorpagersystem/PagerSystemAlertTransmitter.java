package erserver.modules.dependencies.vendorpagersystem;

import erserver.modules.dependencies.AlertTransmitter;
import erserver.modules.dependencies.vendorpagersystem.PagerTransport;

public class PagerSystemAlertTransmitter implements AlertTransmitter {

    @Override
    public void transmit(String targetDevice, String pageText) {
        PagerTransport transport = getInitializedTransport();
        transport.transmit(targetDevice,pageText);
    }

    @Override
    public void transmitRequiringAcknowledgement(String targetDevice, String pageText) {
        PagerTransport transport = getInitializedTransport();
        transport.transmitRequiringAcknowledgement(targetDevice,pageText);
    }

    private PagerTransport getInitializedTransport() {
        PagerTransport transport = PagerSystem.getTransport();
        transport.initialize();
        return transport;
    }

}
