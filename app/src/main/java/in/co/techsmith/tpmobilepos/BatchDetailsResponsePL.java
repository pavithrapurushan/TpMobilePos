package in.co.techsmith.tpmobilepos;

import java.util.ArrayList;
import java.util.List;

public class BatchDetailsResponsePL {

    public BatchDetailsResponsePL() {
        Batch = new ArrayList<BatchDetails>();
    }
    public List<BatchDetails> Batch;
    public int ErrorStatus;
    public String Message;
    
}
