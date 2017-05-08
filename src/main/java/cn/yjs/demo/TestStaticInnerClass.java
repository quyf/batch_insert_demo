package cn.yjs.demo;

public class TestStaticInnerClass {
	
	private static String sre;
	private static class ResourceHolder{
		static{
			System.out.println("static inner");
			sre = "ssss";
		}
		public static String inner = "inner str";
	}
	
	static{
		System.out.println("after...");
	}
	
	public static String get(){
		return ResourceHolder.inner;
	}
	
	public static void main(String[] args) {
		//System.out.println( TestStaticInnerClass.get());
		
		TestStaticInnerClass cl = new TestStaticInnerClass();
		System.out.println(cl.sre);
		cl.get();
		System.out.println(cl.sre);
	}
}
