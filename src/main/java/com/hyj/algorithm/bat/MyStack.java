package com.hyj.algorithm.bat;

/**
 * 堆栈
 * @param <T>
 */
public class MyStack<T> {
	//表头
	private LinkNode front;
	//β�ڵ�
	private LinkNode rear;
	
	//�ж��Ƿ�Ϊ��
	public boolean isEmpty(){
		return front == null;
	}
	//��ջ
	public void push(T data){
		LinkNode node = new LinkNode(data);
		node.setNext(front);
		front = node;
	}
	//��ջ
	public T pop(){
		LinkNode next = front.getNext();
		T data = (T) front.getData();
		front = next;
		return data;
	}
}
