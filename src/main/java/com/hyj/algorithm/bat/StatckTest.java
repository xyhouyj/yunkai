package com.hyj.algorithm.bat;

import com.sun.jndi.toolkit.ctx.StringHeadTail;

public class StatckTest {
	public static void main(String[] args) {
		String oneStr = "(({})[])[()}]";
//		boolean ifKuoHao = kuoHaoPiPei(oneStr);
//		System.out.println("�����Ƿ�ƥ��Ľ��===" + ifKuoHao);
		String twoStr = "))";
//		Integer maxLength = getMaxKuohao(twoStr);
//		System.out.println("��ȡ�ƥ�����ų���==="+maxLength);
		
		//�沨�����ʽ����
		char[] charArray = {'2','1','+','3','*'};
		getResult(charArray);
	}
	/**
	 * �沨�����ʽ ��Ϊ��׺���ʽ ����ӡ���
	 * @param charArray
	 */
	private static void getResult(char[] charArray) {
		// TODO Auto-generated method stub
		MyStack<Integer> myStack = new MyStack<Integer>();
		MyStack<String> zhongStack = new MyStack<String>();
		for (Character c : charArray) {
			if (!isOperation(c)) {
				myStack.push(new Integer(c.toString()));
			}else {
				Integer two = myStack.pop();
				Integer one = myStack.pop();
				if (c=='+') {
					myStack.push(one+two);
				}
				if (c=='-') {
					myStack.push(one-two);
				}
				if (c=='*') {
					myStack.push(one*two);
				}
				if (c=='/') {
					myStack.push(one/two);
				}
			}
		}
		System.out.println(myStack.pop());
	}
	private static boolean isOperation(char c) {
		return c=='+' || c=='-'||c=='*' || c=='/';
	}
	/**
	 * �������ŵĳ���
	 * @param twoStr
	 * @return
	 */
	private static Integer getMaxKuohao(String twoStr) {
		int ml = 0;
		MyStack<Character> myStack = new MyStack<Character>(); 
		MyStack<Integer> myStack2 = new MyStack<Integer>();
		char[] charArray = twoStr.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			if (isLeftKuoHao(charArray[i])) {
				myStack.push(charArray[i]);
				myStack2.push(i);
			}else {
				if (!myStack.isEmpty()) {
					Character pop = myStack.pop();
					Integer pop2 = myStack2.pop();
					if (ifMathch(pop,charArray[i])) {
						int length = i-pop2 +1;
						if (length > ml) {
							ml = length;
						}
					}
				}
			}
		}
		return ml;
	}
	/**
	 * �ж������Ƿ�ƥ��
	 * @param oneStr
	 * @return
	 */
	private static boolean kuoHaoPiPei(String oneStr) {
		boolean flag = true;
		MyStack<Character> myStack = new MyStack<Character>(); 
		char[] charArray = oneStr.toCharArray();
		for (char character : charArray) {
			//�ж� ����������� ��ջ
			if (isLeftKuoHao(character)) {
				myStack.push(character);
			}else {
				//����������  ��ȡջ��Ԫ�� ���бȽ�
				if (myStack.isEmpty()) {
					flag = false;
				}else {
					Character pop = myStack.pop();
					if (!ifMathch(pop,character)) {
						//���ƥ��
						flag = false;
					}
					
				}
				
				
			}
		}
		return flag;
	}
	/**
	 * �Ƚ�ջ��Ԫ�� �͵�ǰԪ���Ƿ�ƥ��
	 * @param pop
	 * @param character
	 * @return
	 */
	private static boolean ifMathch(Character pop, Character character) {
		if (pop=='(' && character==')') {
			return true;
		}
		if (pop=='[' && character==']') {
			return true;
		}
		if (pop=='{' && character=='}') {
			return true;
		}
		return false;
	}
	private static boolean isLeftKuoHao(Character character) {
		
		return character=='(' || character=='{' || character=='[';
	}
}
