package erserver.modules.dependencies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlertTransmitterTestDouble implements AlertTransmitter{

    private List<String> alertsReceived;
    private List<String> alertsReceivedRequiringAck;

    public AlertTransmitterTestDouble(){
        this.alertsReceived = new ArrayList<>();
        this.alertsReceivedRequiringAck = new ArrayList<>();
    }

    @Override
    public void transmit(String targetDevice, String pageText) {
        this.alertsReceived.add(targetDevice + ": " + pageText);
    }

    @Override
    public void transmitRequiringAcknowledgement(String targetDevice, String pageText) {
        this.alertsReceivedRequiringAck.add(targetDevice + ": " + pageText);
    }

    public List<String> getAlertsReceived() {
        return Collections.unmodifiableList(alertsReceived);
    }

    public List<String> getAlertsReceivedRequiringAck() {
        return Collections.unmodifiableList(alertsReceivedRequiringAck);
    }
}
