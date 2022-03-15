package smartmg.helper;

public class ResponseMessage {

	private String content;
	private String type;
	
	public ResponseMessage() {
		super();
	}
	public ResponseMessage(String content, String type) {
		super();
		this.content = content;
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "ResponseMessage [content=" + content + ", type=" + type + "]";
	}
	
	
	
	
	
}
