package utils;

public class Pass {

	public static void main(String[] args) {
		PasswordAuthentication p = new PasswordAuthentication();
		char [] password = {'O','p','e','r','a','t','o','r'};
		System.out.println(p.hash(password));

	}

}
