package app.socket;
  
import java.io.Serializable;  
  
public class ResponseObject implements Serializable {  
  
    private static final long serialVersionUID = 1L;  
  
    private String value;  
  
    private byte[] bytes;  
      
    public ResponseObject(String value) {  
        this.value = value;  
        this.bytes = new byte[1024];  
    }  
      
    public ResponseObject() {
		
	}

    public String getValue() {  
        return value;  
    }  
  
    public void setValue(String value) {  
        this.value = value;  
    }  
      
    @Override  
    public String toString() {  
        StringBuffer sb = new StringBuffer();  
        sb.append("Response [output: " + value + ", bytes: " + bytes.length+ "]");  
        return sb.toString();  
    }  
}  
