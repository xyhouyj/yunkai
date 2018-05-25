package com.hyj.algorithm.bat;

public class LinkNode<T> {
	
	private T data;

	public LinkNode(T data) {
		this.data = data;
	}
	
	private LinkNode next;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public LinkNode getNext() {
		return next;
	}

	public void setNext(LinkNode next) {
		this.next = next;
	}

	public LinkNode() {
	}
}
