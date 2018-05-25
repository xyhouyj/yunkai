package com.hyj.algorithm.bat;

public class LinkAddTest {
	public static void main(String[] args) throws Exception {
		//生成两个链表
		LinkNode<Integer> one = new LinkNode<Integer>(4);
		LinkNode<Integer> one1 = new LinkNode<Integer>(4);
		LinkNode<Integer> one2 = new LinkNode<Integer>(3);
		one.setNext(one1);
		one1.setNext(one2);
		//链表去重
		LinkNode<Integer> one3 = new LinkNode<Integer>(3);
		LinkNode<Integer> one4 = new LinkNode<Integer>(5);
		LinkNode<Integer> one5 = new LinkNode<Integer>(5);
		LinkNode<Integer> one6 = new LinkNode<Integer>(5);
		LinkNode<Integer> one7 = new LinkNode<Integer>(7);
		one2.setNext(one3);
		one3.setNext(one4);
		one4.setNext(one5);
		one5.setNext(one6);
		one6.setNext(one7);
//		removeSameElement(one);
//		removeSameElementFirst(one);
//		removeSameElementAll(one);
		
		LinkNode<Integer> two = new LinkNode<Integer>(5);
		LinkNode<Integer> two1 = new LinkNode<Integer>(6);
		LinkNode<Integer> two2 = new LinkNode<Integer>(4);
		LinkNode<Integer> two3 = new LinkNode<Integer>(2);
		two.setNext(two1);
		two1.setNext(two2);
		two2.setNext(two3);
		//两个链表相加
//		addTwoLink(one,two);
		//链表反转
		LinkNode<Integer> two4 = new LinkNode<Integer>(7);
		LinkNode<Integer> two5 = new LinkNode<Integer>(8);
		LinkNode<Integer> two6 = new LinkNode<Integer>(9);
		LinkNode<Integer> two7 = new LinkNode<Integer>(1);
		two3.setNext(two4);
		two4.setNext(two5);
		two5.setNext(two6);
		two6.setNext(two7);
//		reverseLink(two,3,7);
		fenByValue(two,4);
		
	}
	/**
	 * 根据 给定的额值 对链表进行划分
	 * @param two
	 * @param i
	 */
	private static void fenByValue(LinkNode<Integer> two, int i) {
		LinkNode<Integer> lessValue = new LinkNode<Integer>();
		LinkNode<Integer> p = lessValue;
		LinkNode<Integer> moreValue = new LinkNode<Integer>();
		LinkNode<Integer> q = moreValue;
		while(null != two){
			Integer value = two.getData();
			LinkNode<Integer> valueNode = new LinkNode<Integer>(value);
			if(value <= i){
				lessValue.setNext(valueNode);
				lessValue = lessValue.getNext();
			}else{
				moreValue.setNext(valueNode);
				moreValue = moreValue.getNext();
			}
			two = two.getNext();
		}
		lessValue.setNext(q.getNext());
		p = p.getNext();
		while(null != p){
			System.out.println(p.getData());
			p = p.getNext();
		}
		
	}
	//删除所有重复的元素
	private static void removeSameElementAll(LinkNode<Integer> one) {
		
		LinkNode<Integer> pCur = one.getNext();
		LinkNode<Integer> pNext = null;
		while(null != pCur && null != one){
			pNext = pCur.getNext();
			boolean ifSame = false;
			while(null != one && one.getData() == pCur.getData()){
				one.setNext(pNext);
				pCur = pNext;
				pNext = pNext.getNext();
				ifSame = true;
			}
			//如果是重复的   one直接取他的下一个链的值
			if(ifSame){
				one = one.getNext();
			}else{
				one = pCur;
			}
			pCur = pNext;
		}
		
		System.out.println("---------------");
		while(null != one){
			System.out.println(one.getData());
			one = one.getNext();
		}
	}
	/**
	 * 链表去重  -----保留最后一次出现的元素
	 * @param one
	 */
	private static void removeSameElementFirst(LinkNode<Integer> one) {
		// TODO Auto-generated method stub
		LinkNode<Integer> result = one;
		while(null != one){
			System.out.println(one.getData());
			one = one.getNext();
		}
		//三个遍历  one   pCur  pNext
		one = result;
		LinkNode<Integer> pCur = one.getNext();
		LinkNode<Integer> pNext = null;
		while(null != pCur && null != one){
			pNext = pCur.getNext();
			while(null != one && one.getData() == pCur.getData()){
				one.setNext(pNext);
				pCur = pNext;
				pNext = pNext.getNext();
			}
			one = pCur;
			pCur = pNext;
		}
		
		System.out.println("---------------");
		while(null != result){
			System.out.println(result.getData());
			result = result.getNext();
		}
	}
	/**
	 * 链表去重复 --- 保留第一次出现的元素
	 * @param one
	 */
	private static void removeSameElement(LinkNode<Integer> one) {
		LinkNode<Integer> result = one;
		while(null != one){
			System.out.println(one.getData());
			one = one.getNext();
		}
		one = result;
		LinkNode<Integer> p = one;
		LinkNode<Integer> q = one.getNext();
		//遍历 判断当前值 是否跟next的元素的值相等
		while(null != p && null != q){
			Integer valueP = p.getData();
			Integer valueQ = q.getData();
			if(valueP == valueQ){
				//相等的话  
				p.setNext(q.getNext());
				q = q.getNext();
			}else{
				//不相等  直接下移
				p = p.getNext();
				q = q.getNext();
			}
		}
		p = null;
		q = null;
		System.out.println("---------------");
		while(null != result){
			System.out.println(result.getData());
			result = result.getNext();
		}
	}
	/**
	 * 翻转link  从start  到end处的进行翻转
	 * @param two
	 * @param start
	 * @param end
	 * @throws Exception 
	 */
	private static void reverseLink(LinkNode<Integer> two, int start, int end) throws Exception {
		LinkNode<Integer> result = two;
		int linkLength = getLinkLength(two);
		System.out.println("the length of the link is " + linkLength);
		if (start > linkLength || end>linkLength) {
			throw new Exception("start must be lesser than length of link,end must be lesser than length of link");
		}
		//找到开始翻转的前一个节点
		for (int i = 0; i < start-1 ; i++) {
			two = two.getNext();
		}
		LinkNode<Integer> tmpOne= two.getNext();
		LinkNode<Integer> nodeFour = tmpOne;//
		LinkNode<Integer> tmpTwo = tmpOne.getNext();
		//切断tmpOne跟之后节点的连写
		tmpOne.setNext(null);
		LinkNode<Integer> po = null;
		//进行翻转
		for(int i = 0;i<end -start-1 ;i++){
			po = tmpTwo;
			tmpTwo = tmpTwo.getNext();
			po.setNext(tmpOne);
			tmpOne = po;
		}
		//翻转前的two 设置为最后需要翻转的
		two.setNext(po);
		nodeFour.setNext(tmpTwo);
		//释放临时变量
		tmpOne = null;
		po = null;
		tmpTwo = null;
		//打印结果
		while(null != result){
			System.out.println(result.getData());
			result = result.getNext();
		}
	}
	/**
	 * add two links
	 * @param one
	 * @param two
	 */
	private static void addTwoLink(LinkNode<Integer> one, LinkNode<Integer> two) {
		LinkNode<Integer> result = new LinkNode<Integer>(0);
		
		LinkNode<Integer> tmp = result;
		//确定两个链表的长度
		int oneLength = getLinkLength(one);
		int twoLength = getLinkLength(two);
		System.out.println("one link's length is " + oneLength);
		System.out.println("two link's length is " + twoLength);
		//确定需要加的次数
		int numsToAdd = oneLength > twoLength?twoLength:oneLength;
		int jinWeiValue = 0;
		int linkValue = 0;
		for (int i = 0; i < numsToAdd; i++) {
			int resultOfAdd = one.getData() + two.getData();
			//相加值 对10的余数  加上进位值 就是当前位的值
			linkValue = resultOfAdd % 10 + jinWeiValue;
			jinWeiValue = resultOfAdd/10;
			one = one.getNext();
			two = two.getNext();
			LinkNode<Integer> link = new LinkNode<Integer>(linkValue);
			tmp.setNext(link);
			tmp = tmp.getNext();//链表下移
		}
		
		if (numsToAdd == oneLength) {
			//遍历twoLength
			while (null != two) {
				int resultOfAdd = two.getData()+jinWeiValue;
				linkValue = resultOfAdd%10 + jinWeiValue;
				jinWeiValue = resultOfAdd/10;
				two = two.getNext();
				LinkNode<Integer> link = new LinkNode<Integer>(linkValue);
				tmp.setNext(link);
				tmp = tmp.getNext();
			}
		}
		if (numsToAdd == twoLength) {
			while (null != one) {
				int resultOfAdd = one.getData()+jinWeiValue;
				linkValue = resultOfAdd%10 + jinWeiValue;
				jinWeiValue = resultOfAdd/10;
				one = one.getNext();
				LinkNode<Integer> link = new LinkNode<Integer>(linkValue);
				tmp.setNext(link);
				tmp = tmp.getNext();
			}
		}
		tmp = null;
		result = result.getNext();
		while(null != result){
			System.out.println(result.getData());
			result = result.getNext();
		}
		
	}
	private static int getLinkLength(LinkNode<Integer> one) {
		int num = 0;
		while (null != one) {
			num++;
			one = one.getNext();
		}
		return num;
	}
}
