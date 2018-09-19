package ro.msg.learning.shop.utilities;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;

public class CsvConverter<T> extends AbstractGenericHttpMessageConverter<List<T>> {

    //TODO finish this csv converter

    public CsvConverter(){
        super(new MediaType("text","csv"));
    }

    @Override
    protected List<T> readInternal(Class<? extends List<T>> clazz, HttpInputMessage inputMessage) throws IOException {
        return fromCsv(null, null,inputMessage.getBody());
    }

    @Override
    protected void writeInternal(List<T> t, Type type, HttpOutputMessage outputMessage) throws IOException {
        toCsv(null, null, t,outputMessage.getBody());
    }

    @Override
    public List<T> read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException {
        return fromCsv(null, null,inputMessage.getBody());
    }

    public List<T> fromCsv(T pojoType, Class<T> pojoObject, InputStream inputStream) throws IOException {

        if (pojoType==null)
            return null;

        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(pojoType.getClass());
        MappingIterator<T> it = mapper.readerFor(pojoObject).with(schema).readValues(inputStream);

        return it.readAll();
    }

    public void toCsv(T pojoType, Class<T> pojoObject, List<T> pojosWritten, OutputStream outputStream) throws IOException {

        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(pojoType.getClass());
        String csv = mapper.writer(schema).writeValueAsString(pojosWritten);

        outputStream.write(csv.getBytes());
//        return csv;
    }


//    @Override
//    protected void writeInternal(T t, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
//        // t - input, obiectele care vin
//        // type - se ia clasa
//        // outputMessage - aici se scriu rezultatele
//
//        toCsv(t, (Class<T>) type,(List<T>)outputMessage,outputMessage.getBody());
//
//
//    }
//
//    @Override
//    protected T readInternal(Class<? extends T> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
//        return null;
//    }
//
//    @Override
//    public T read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
//        // type - se ia clasa
//        // class - si de aici se ia clasa
//        // inputMessage - de aici se ia stringu pt conversie
//
//        return null;
//    }
}
