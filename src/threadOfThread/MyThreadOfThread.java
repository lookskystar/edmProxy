package threadOfThread;

//多线程中套用多线程例子
public class MyThreadOfThread {

	public static void main(String[] args) {
		StartTaskIP startTaskIP=new StartTaskIP();
		Runnable r=new MyThreadIP(startTaskIP);
		Thread t=new Thread(r);
		t.start();
	}
}

//线程类，启动多任务Ip
class MyThreadIP implements Runnable{
	private StartTaskIP startTaskIP;
	public MyThreadIP(StartTaskIP startTaskIP){
		this.startTaskIP=startTaskIP;
	}
	public void run() {
		startTaskIP.startIP();
	}
}
//具体实现类 IP
class  StartTaskIP{
	public synchronized void startIP(){
		//System.out.println("开始IP");
		//启动账号线程
		StartTaskAcc startTaskAcc=new StartTaskAcc();
		for (int i = 0; i < 5; i++) {
			System.out.println("开启"+i+"任务IP");
			Runnable r=new MyThreadAcc(i,7,startTaskAcc);
			Thread t=new Thread(r);
			t.start();
		}
		
		
	}
}
//线程类，启动多任务账号
class MyThreadAcc implements Runnable{
	private int num;
	private StartTaskAcc startTaskAcc;
	private int accNum;
	public MyThreadAcc(int num,int accNum,StartTaskAcc startTaskAcc){
		this.num=num;
		this.startTaskAcc=startTaskAcc;
		this.accNum=accNum;
	}
	public void run() {
		startTaskAcc.startAcc(num,accNum);
	}
}
//具体实现类 账号
class  StartTaskAcc{
	public synchronized void startAcc(int num,int accNum){
		//System.out.println("开始账号");
		for (int i = 0; i < accNum; i++) {
			System.out.println("开启第"+num+"个ip，第"+i+"个账号");
		}
		
	}
}

