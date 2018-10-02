package ro.msg.learning.shop.utilities;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.stereotype.Component;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class CsvConverter<T> extends AbstractGenericHttpMessageConverter<List<T>> {

    @Override
    @SneakyThrows
    public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
        return type instanceof ParameterizedType &&
            Class.forName(((ParameterizedTypeImpl) type).getRawType().getName()).equals(List.class) &&
            mediaType != null &&
            mediaType.getType().equals("text") &&
            mediaType.getSubtype().equals("csv");
    }

    @Override
    @SneakyThrows
    public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
        return type instanceof ParameterizedType &&
            Class.forName(((ParameterizedTypeImpl) type).getRawType().getName()).equals(List.class) &&
            mediaType != null &&
            mediaType.getType().equals("text") &&
            mediaType.getSubtype().equals("csv") &&
            super.canWrite(type, clazz, mediaType);
    }

    public CsvConverter() {
        super(new MediaType("text", "csv"));
    }

    protected List<T> readInternal(Class<? extends List<T>> clazz, HttpInputMessage inputMessage) throws IOException {
        return read(clazz.getClass(), clazz, inputMessage);
    }

    @Override
    protected void writeInternal(List<T> ts, HttpOutputMessage outputMessage) throws IOException {
        this.writeInternal(ts, ts.getClass(), outputMessage);
    }

    @Override
    protected void writeInternal(List t, Type type, HttpOutputMessage outputMessage) throws IOException {
        // t - input, obiectele care vin
        // type - se ia clasa
        // outputMessage - aici se scriu rezultatele

        try {
            toCsv((Class.forName(((ParameterizedType) type).getActualTypeArguments()[0].getTypeName())), t, outputMessage.getBody());
        } catch (ClassNotFoundException e) {
            log.error("Given class was not found!", e);
        }
    }

    @Override
    public List read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException {
        // type - se ia clasa
        // class - si de aici se ia clasa
        // inputMessage - de aici se ia stringu pt conversie

        try {
            return fromCsv((Class.forName(((ParameterizedType) type).getActualTypeArguments()[0].getTypeName())), inputMessage.getBody());
        } catch (ClassNotFoundException e) {
            log.error("Given class was not found!", e);
        }

        return Collections.emptyList();
    }

    public List<T> fromCsv(Class clazz, InputStream inputStream) throws IOException {

        if (clazz == null) {
            return null;
        }

        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = CsvSchema.emptySchema().withHeader();

        MappingIterator<T> it = mapper.readerFor(clazz).with(schema)
            .readValues(inputStream);

        return it.readAll();
    }

    public void toCsv(Class clazz, List<T> pojosWritten, OutputStream outputStream) throws IOException {

        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(clazz).withHeader().withColumnReordering(false);

        String csv = mapper.writer(schema).writeValueAsString(pojosWritten);

        PrintStream printStream = new PrintStream(outputStream);
        printStream.write(csv.getBytes());
    }
}
