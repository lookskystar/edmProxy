package httpProxySrc.proxy;

//�����̣߳�

import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;

import java.net.Socket;

/**
 * ������
 * 
 * @author $����ǿ$
 * @version $Revision$
 */
public class DataService implements Runnable {

	int CONNECT_RETRIES = 5;
	int CONNECT_PAUSE = 5;
	int TIMEOUT = 50; // socket��ʱ����
	int BUFSIZ = 1024 * 5; // ������
	boolean loging = true; // �Ƿ������־��Ϣ

	// �ʹ��̹߳�����Socket�����ںͿͻ��˽�������
	Socket socket;

	// �����־
	public void log(String log) {

		if (loging) {

			System.out.println(log);
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param s
	 *            ������
	 */
	public DataService(Socket s) {

		socket = s;
	}

	// �������ÿһ��ͼԪ���з�����߳�
	public void run() {

		String line;
		String host;
		int port = 80; // Ĭ��
		Socket outbound = null;

		try {

			socket.setSoTimeout(TIMEOUT);

			InputStream is = socket.getInputStream();
			OutputStream os = null;

			// ��ȡ�����е�����
			line = "";
			host = "";

			int state = 0;
			boolean space;

			while (true) {

				int c = is.read();

				if (c == -1) {

					break;
				}

				space = Character.isWhitespace((char) c);

				switch (state) {

				case 0:

					if (space) {

						continue;
					}

					state = 1;

				case 1:

					if (space) {

						state = 2;

						continue;
					}

					line = line + (char) c;

					break;

				case 2:

					if (space) {

						continue; // ��������հ��ַ�
					}

					state = 3;

				case 3: // ----------------------------------------------------------

					if (space) {

						state = 4;

						// ֻȡ���������Ʋ���
						String host0 = host;
						int n;
						n = host.indexOf("//");

						if (n != -1) {

							host = host.substring(n + 2);
						}

						n = host.indexOf('/');

						if (n != -1) {
							host = host.substring(0, n);
						}

						// �������ܴ��ڵĶ˿ں�
						n = host.indexOf(":");

						if (n != -1) {

							port = Integer.parseInt(host.substring(n + 1));
							host = host.substring(0, n);
						}

						// ��������ϼ����������ý������ϲ�����ֱ�Ӻʹ���ͨѶ

						int retry = CONNECT_RETRIES;

						// ��������Զ��Ŀ�꣬CONNECT_RETRIES��
						while (retry-- != 0) {

							try {

								outbound = new Socket(host, port);

								break;
							} catch (Exception e) {

								// System.out.println ("����...");
							}

							// �ȴ�
							Thread.sleep(CONNECT_PAUSE);
						}

						if (outbound == null) {

							break;
						}

						outbound.setSoTimeout(TIMEOUT);
						os = outbound.getOutputStream(); // ��Զ�̷�����д������
						os.write(line.getBytes());
						os.write(' ');
						os.write(host0.getBytes());
						os.write(' ');

						pipe(is, outbound.getInputStream(), os, socket
								.getOutputStream());

						break; // ����case 3
					}

					host = host + (char) c;

					break;

				// -------------------------------------------------------------
				}
			}
		} catch (Exception e) {

		} finally {

			try {

				socket.close();
			} catch (Exception e1) {

			}

			try {

				outbound.close();
			} catch (Exception e2) {

			}
		}
	}

	/**
	 * ���������ݴ�����
	 * 
	 * @param is0
	 *            ��������ȡ�ͻ���������Ϣ
	 * @param is1
	 *            ��������ȡԶ�̷�������������
	 * @param os0
	 *            ��������Զ�̷�����д��������Ϣ
	 * @param os1
	 *            ��������ͻ���д���������������
	 */
	void pipe(InputStream is0, InputStream is1, OutputStream os0,
			OutputStream os1) {

		try {

			int ir;
			byte[] bytes = new byte[BUFSIZ];

			while (true) {

				try {

					if ((ir = is0.read(bytes)) > 0) {

						os0.write(bytes, 0, ir);

						// System.out.println (new String(bytes).trim ());
					} else if (ir < 0) {

						break;
					}
				} catch (InterruptedIOException e) {

				}

				try {

					if ((ir = is1.read(bytes)) > 0) {

						os1.write(bytes, 0, ir);
					} else if (ir < 0) {

						break;
					}
				} catch (InterruptedIOException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e0) {

			//System.out.println ("Pipe�쳣: " + e0);
		}
	}
}