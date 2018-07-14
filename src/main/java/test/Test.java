package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Test {

	public static void main(String[] args) {
		List<String> list = new ArrayList();
		Optional<String> findFirst = list.stream().findFirst();
		
	}
}
