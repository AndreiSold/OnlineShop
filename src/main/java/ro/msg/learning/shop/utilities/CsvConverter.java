package ro.msg.learning.shop.utilities;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class CsvConverter<T> extends AbstractGenericHttpMessageConverter<List<T>> {


    @Override
    public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
        try {
            return super.canRead(type, Class.forName(((ParameterizedType) type).getActualTypeArguments()[0].getTypeName()), mediaType);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //return (type instanceof Class ? canRead((Class<?>) type, mediaType) : canRead(mediaType));
        return false;
    }

    @Override
    public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
//        return super.canWrite(type, clazz, mediaType);
//        return canWrite(clazz, mediaType);
        try {
            return super.canWrite(type, Class.forName(((ParameterizedType) type).getActualTypeArguments()[0].getTypeName()), new MediaType("text", "csv"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }


    //TODO csv writer working, reader not working yet


    public CsvConverter() {
        super(new MediaType("text", "csv"));

    }

    //    @Override
//    protected List<T> readInternal(Class<? extends List<T>> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
////        return fromCsv((T) clazz.getClass(), (Class<T>) clazz, inputMessage.getBody());
//        return fromCsv(clazz.getClass(), inputMessage.getBody());
//    }
    protected List<T> readInternal(Class<? extends List<T>> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
//        CSVReader reader = new CSVReader(new InputStreamReader(inputMessage.getBody()));
//
//        Object[] rows= reader.readAll().toArray();
//
//        String bla="";
//
//        for (String[] row : rows)
//            for (int i=0; i<row.length;i++)
//                bla+=row[i];


        return Collections.emptyList();
    }

//    @Override
//    protected List readInternal(Class<? extends List> clazz, HttpInputMessage inputMessage) throws IOException {
//     //   return fromCsv(null, null, inputMessage.getBody());
//        return null;
//    }

    @Override
    protected void writeInternal(List t, Type type, HttpOutputMessage outputMessage) throws IOException {
        // t - input, obiectele care vin
        // type - se ia clasa
        // outputMessage - aici se scriu rezultatele

        //type.getActualTypeArguments()[0]
        try {
            toCsv((Class.forName(((ParameterizedType) type).getActualTypeArguments()[0].getTypeName())), t, outputMessage.getBody());
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException {
        // type - se ia clasa
        // class - si de aici se ia clasa
        // inputMessage - de aici se ia stringu pt conversie

//        try {
////            return fromCsv((T) Class.forName(((ParameterizedType) type).getActualTypeArguments()[0].getTypeName()), (Class<T>) contextClass, inputMessage.getBody());
//            return fromCsv((Class.forName(((ParameterizedType) type).getActualTypeArguments()[0].getTypeName())), inputMessage.getBody());
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

        return Collections.emptyList();
    }

//    public List<T> fromCsv(T pojoType, Class<T> pojoObject, InputStream inputStream) throws IOException {
//
//        if (pojoType == null)
//            return null;
//
//        CsvMapper mapper = new CsvMapper();
//        CsvSchema schema = mapper.schemaFor(pojoType.getClass());
//        BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
//
//        String line=reader.readLine();
//
//        String[] fields=line.split(",");
//
//        MappingIterator<T> it = mapper.readerFor(pojoObject).with(schema).readValues(inputStream);
//
//        return it.readAll();
//    }

    public List<T> fromCsv(Class clazz, InputStream inputStream) throws IOException {

//        if (clazz == null)
//            return null;

//        CsvMapper mapper = new CsvMapper();
//        CsvSchema schema = mapper.schemaFor(clazz.getClass());
//        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//
//        String line = reader.readLine();
//
//        String[] fields = line.split(",");
//
//        MappingIterator<T> it = mapper.readerFor(clazz).with(schema).readValues(inputStream);

//        return it.readAll();
        return null;
    }


    public void toCsv(Class clazz, List<T> pojosWritten, OutputStream outputStream) throws IOException {

        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(clazz);

        String csv = "";

        List<Field> fieldList = Arrays.asList(clazz.getDeclaredFields());

        for (Field field : fieldList)
            if (fieldList.get(0).equals(field))
                csv += field.getName();
            else
                csv += "," + field.getName();

        csv += "\n";

        for (T pojo : pojosWritten)
            csv = csv + mapper.writer(schema).writeValueAsString(pojo);

        //Here you have the csv result as string

        PrintStream printStream = new PrintStream(outputStream);
        printStream.write(csv.getBytes());
    }
}
