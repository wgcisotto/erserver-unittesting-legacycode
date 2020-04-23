package erserver.modules.dependencies;

public interface EmergencyResponseProvider {
    void requestInboundDiversion(Priority priority);

    void removeInboundDiversion(Priority priority);
}
