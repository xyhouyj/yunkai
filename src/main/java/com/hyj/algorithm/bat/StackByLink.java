package com.hyj.algorithm.bat;

public class StackByLink {
	private LinkNode front;//栈顶指针
	
	private LinkNode rear;//栈底指针
	
	public boolean isEmpty(){
		return front ==null;
	}
	
	public void print(){
		LinkNode node = front;
		while(null != null){
			System.out.println(node.getData());
			node = node.getNext();
		}
	}
	
	//压入方法
	public void push(int data){
		LinkNode node = new LinkNode(data);
		if (isEmpty()) {
			front = node;
			rear = node;
		}else {
			node.setNext(front);
			front = node;
		}
	}
	
	//出栈方法
	public void pop(){
		LinkNode node;
		if (isEmpty()) {
			System.out.println("目前栈空");
			return ;
		}
		node = front;
		if (node == rear) {
			front = null;
			rear = null;
			System.out.println("操作之后 目前空栈");
		}else {
			front = front.getNext();
		}
	}
}
