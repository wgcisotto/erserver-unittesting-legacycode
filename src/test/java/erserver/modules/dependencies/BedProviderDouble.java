package erserver.modules.dependencies;

import java.util.ArrayList;
import java.util.List;

public class BedProviderDouble implements BedProvider {
    @Override
    public List<Bed> getAllBeds() {
        return new ArrayList<>();
    }
}
