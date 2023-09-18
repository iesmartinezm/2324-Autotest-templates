package clases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.JavaSource;

import clases.helpers.MyReflection;

public abstract class TestBase {
	

	protected final PrintStream standardOut = System.out;
	protected final InputStream standardIn = System.in;
	protected final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
	
	@BeforeEach
	void setUp() {
		System.setOut(new PrintStream(outputStreamCaptor));
	}
	
	@AfterEach
	void tearDown() {
		System.setOut(standardOut);
		System.setIn(standardIn);
	}
	
	protected void setIn(String entrada) {
		System.setIn(new ByteArrayInputStream(entrada.getBytes()));
	}
	
	void pruebaSetGetString(Object obj, String className, String fieldName, String expValue) throws Exception {
		MyReflection.ejecutaMetodoSet(obj, className, fieldName,
				Arrays.asList(expValue).toArray(), String.class);
		pruebaGetString(obj, className, fieldName, expValue);
	}
	
	void pruebaSetGetLocalDate(Object obj, String className, String fieldName, LocalDate expValue) throws Exception {
		MyReflection.ejecutaMetodoSet(obj, className, fieldName,
				Arrays.asList(expValue).toArray(), LocalDate.class);
		pruebaGetLocalDate(obj, className, fieldName, expValue);
	}
	
	@SuppressWarnings("rawtypes")
	void pruebaSetGetList(Object obj, String className, String fieldName, List expValue) throws Exception {
		MyReflection.ejecutaMetodoSet(obj, className, fieldName,
				Arrays.asList(expValue).toArray(), List.class);
		pruebaGetList(obj, className, fieldName, expValue);
	}
	
	String pruebaGetString(Object obj, String className, String fieldName, String expValue) throws Exception {
		String actValue = (String) MyReflection.ejecutaMetodoGet(obj, className, fieldName,	null);
		assertEquals(expValue,actValue);
		return actValue;
	}
	
	LocalDate pruebaGetLocalDate(Object obj, String className, String fieldName, LocalDate expValue) throws Exception {
		LocalDate actValue = (LocalDate) MyReflection.ejecutaMetodoGet(obj, className, fieldName,null);
		assertEquals(expValue,actValue);
		return actValue;
	}
	
	@SuppressWarnings("rawtypes")
	List pruebaGetList(Object obj, String className, String fieldName, List expValue) throws Exception {
		List actValue = (List) MyReflection.ejecutaMetodoGet(obj, className, fieldName,null);
		assertTrue(Arrays.deepEquals(expValue.toArray(), actValue.toArray()));
		return actValue;
	}
	
	void constructorDefecto(String className) throws Exception {
		Object object = MyReflection.invocaConstructor(className);
		assertNotNull(object, "No creado");
	}

	void println(Object message) {
		this.standardOut.println(message);
	}
	
	void print(Object message) {
		this.standardOut.print(message);
	}
	
	void compruebaAtributos(String className, String [][] atributos) throws Exception {
				
		for (String[] atributo : atributos) {
			assertEquals(atributo[1], MyReflection.getField(className, atributo[0]));
		}
		
	}
	
	void compruebaExistenciaMetodos(String className,List<String> expectedMethods) throws ClassNotFoundException {
		List<String> actualMethods = MyReflection.getPublicMethods(className);
		assertTrue(actualMethods.containsAll(expectedMethods));
	}
	

	void testCompareTo(String className, Object u1menor, Object u2mayor) throws Exception{
		int result = MyReflection.compareTo(className, u1menor,u2mayor);
		assertTrue(result<0);
		
		result = MyReflection.compareTo(className, u2mayor,u1menor);
		assertTrue(result>0);
		
		result = MyReflection.compareTo(className, u1menor,u1menor);
		assertEquals(0,result);
	}

	void compruebaProhibido(String classFullPath, String classForbidden) {
		JavaDocBuilder builder = new JavaDocBuilder();
		try {
			builder.addSource(new FileReader(classFullPath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			assertTrue(false);
		}

		JavaSource src = builder.getSources()[0];
		String[] imports = src.getImports();

		for (String imp : imports) {
			if (imp.endsWith("." + classForbidden)) {
				println("Prohibido importar " + classForbidden + " en la clase " + classFullPath);
				assertTrue(false);
			}
			if (imp.endsWith(".*")) {
				println("Prohibido importar con .*" + " en la clase " + classFullPath);
				assertTrue(false);
			}
		}

		String sc = src.getCodeBlock();

		Pattern pattern = Pattern.compile(classForbidden + "\\.");
		Matcher matcher = pattern.matcher(sc);
		if (matcher.find()) {
			println("Prohibido referenciar " + classForbidden + " en la clase " + classFullPath);
			assertTrue(false);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	boolean isOrderedAscending(Comparable [] array) {
		if(array!=null && array.length>1) {
			for (int i = 0; i < array.length-1; i++) {
				if(array[i].compareTo(array[i+1])>0)
					return false;
			}
		}
		
		return true;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	boolean isOrderedDescending(Comparable [] array) {
		if(array!=null && array.length>1) {
			for (int i = 0; i < array.length-1; i++) {
				if(array[i].compareTo(array[i+1])<0)
					return false;
			}
		}
		
		return true;
	}

}
