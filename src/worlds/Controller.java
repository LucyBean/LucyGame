package worlds;

import org.newdawn.slick.Input;

public class Controller {
	public static int UP = Input.KEY_W;
	public static int DOWN = Input.KEY_S;
	public static int LEFT = Input.KEY_A;
	public static int RIGHT = Input.KEY_D;
	public static int JUMP = Input.KEY_SPACE;
	public static int CROUCH = Input.KEY_S;
	public static int WALK = Input.KEY_LSHIFT;
	public static int INTERACT = Input.KEY_E;
	public static int CAMERA_N = Input.KEY_I;
	public static int CAMERA_S = Input.KEY_K;
	public static int CAMERA_E = Input.KEY_L;
	public static int CAMERA_W = Input.KEY_J;
	public static int CAMERA_IN = Input.KEY_U;
	public static int CAMERA_OUT = Input.KEY_O;
	public static int WORLD_RESET = Input.KEY_T;
	public static int WORLD_PAUSE = Input.KEY_R;
	public static int INVENTORY = Input.KEY_G;
	public static int KICK = Input.KEY_Q;
	
	public static void setDvorak() {
		UP = Input.KEY_COMMA;
		DOWN = Input.KEY_O;
		LEFT = Input.KEY_A;
		RIGHT = Input.KEY_E;
		JUMP = Input.KEY_SPACE;
		CROUCH = Input.KEY_O;
		WALK = Input.KEY_LSHIFT;
		INTERACT = Input.KEY_PERIOD;
		CAMERA_N = Input.KEY_C;
		CAMERA_S = Input.KEY_T;
		CAMERA_E = Input.KEY_N;
		CAMERA_W = Input.KEY_H;
		CAMERA_IN = Input.KEY_R;
		CAMERA_OUT = Input.KEY_G;
		WORLD_RESET = Input.KEY_Y;
		WORLD_PAUSE = Input.KEY_P;
		INVENTORY = Input.KEY_I;
		KICK = Input.KEY_APOSTROPHE;
	}
}
