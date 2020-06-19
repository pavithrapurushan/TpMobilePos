package in.co.tsmith.tpmobilepos;

import java.util.ArrayList;
import java.util.List;

public class BatchDetailsResponsePL {

    public BatchDetailsResponsePL() {
        Batch = new ArrayList<BatchDetails>();
    }
//    public List<BatchDetails> batch;
    public List<BatchDetails> Batch;
    public int ErrorStatus;
    public String Message;
    
}
