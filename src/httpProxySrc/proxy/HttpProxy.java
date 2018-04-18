package httpProxySrc.proxy;

//���ࣺ

import java.io.*;

import java.net.*;

/**
 * ������������������
 * 
 * @author $����ǿ$
 * @version $1.0$
 */
public class HttpProxy {

	/*
	 * /�ϼ�������������ַ�Ͷ˿� private static String parent = null; private static
	 * intparentPort = -1; /** �����������ϼ�������ַ�Ͷ˿ڣ���ѡ����
	 * 
	 * @param name ������ @param pport ������
	 */
	/*
	 * public static void setParentProxy(String name, int pport) { parent =
	 * name; parentPort = pport; }
	 */

	/**
	 * ������������������
	 * 
	 * @param port
	 *            ������
	 */
	public static void startProxy(int port) {

		ServerSocket ssock = null;
		ThreadPool pool = ThreadPool.getInstance();

		try {

			ssock = new ServerSocket(port);
			System.out.println("�ڶ˿�25��������������\n");

			// �������ͬһҳ���ϵĲ�ͬͼԪ���ò�ͬ���߳̽��������
			// �����ò�ͬ���߳̽��з���
			// һ��ҳ��ʹ�õ��߳�������ͼԪ��һ��
			// ���Բ����̳߳��Ǳ�Ҫ��
			while (true) {

				Socket tmp = ssock.accept();

				pool.assign(new DataService(tmp));
			}
		} catch (IOException e) {

			e.printStackTrace();
		} finally {

			pool.complete();

			if (ssock != null) {

				try {

					ssock.close();
				} catch (Exception e) {

				}
			}
		}
	}

	// �����õļ�main����
	public static void main(String[] args) {
		HttpProxy.startProxy(25);
	}
}