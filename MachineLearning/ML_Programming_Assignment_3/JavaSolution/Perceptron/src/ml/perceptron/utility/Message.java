/**
 * 
 */
package ml.perceptron.utility;

/**
 * @author nihatompi
 *
 */

public class Message {

	private boolean m_status;
	private String m_msg;

	public Message(boolean _status, String _msg) {
		// TODO Auto-generated constructor stub
		m_status = _status;
		m_msg = _msg;
	}

	public boolean is_status() {
		return m_status;
	}

	public void set_status(boolean _status) {
		this.m_status = _status;
	}

	public String get_msg() {
		return m_msg;
	}

	public void set_msg(String _msg) {
		this.m_msg = _msg;
	}
}
