package utils;


public class PrintPassword {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PasswordAuthentication au =  new PasswordAuthentication();
		char[] p = {'A','d','m','i','n'};
		System.out.println(p);
		System.out.println(au.hash(p));
	}

}
