package com.dksys.biz.util;

import java.io.Serializable;

/**
 * @version 1.0 클래스 설명 <br>
 *          Obejct 상호간에 호출되는 Object에 대한 정보를 담는 DataModel Class<br>
 */
public class ObjectCallerDM implements Serializable {
	/** Comment for <code>serialVersionUID</code> */
	private static final long serialVersionUID = 3257569516132315702L;
	/**
	 * Caller Class 명 <code>className</code>
	 */
	private String className;
	/**
	 * Call Method 명 <code>methodName</code>
	 */
	private String methodName;
	/**
	 * Call Line Number <code>callLine</code>
	 */
	private int callLine;

	/**
	 * 생성자 ( Caller Class명, Method명, CallLine)
	 * 
	 * @param className
	 * @param methodName
	 * @param callLine
	 */
	public ObjectCallerDM(String className, String methodName, int callLine) {
		this.className = className;
		this.methodName = methodName;
		this.callLine = callLine;
	}

	/**
	 * 호출 Line을 반환한다.
	 * 
	 * @return int callLine
	 */
	public int getCallLine() {
		return callLine;
	}

	/**
	 * 호출 Line을 세팅한다.
	 * 
	 * @param callLine
	 *            callLine
	 */
	public void setCallLine(int callLine) {
		this.callLine = callLine;
	}

	/**
	 * 호출 Class명을 반환한다.
	 * 
	 * @return String className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * 호출 Class명을 세팅한다.
	 * 
	 * @param className
	 *            className
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * 호출 Method명을 반환한다.
	 * 
	 * @return String methodName
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * 호출 Method명을 세팅한다.
	 * 
	 * @param methodName
	 *            methodName
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	/**
	 * CObjectCallerDM의 내용을 반환한다.
	 * 
	 * @see java.lang.Object#toString()
	 * @return String CObjectCallerDM instance 내역
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("Class [" + this.getClassName() + "] ");
		sb.append("Method [" + this.getMethodName() + "] ");
		sb.append("CallLine [" + this.getCallLine() + "]");

		return sb.toString();
	}
}