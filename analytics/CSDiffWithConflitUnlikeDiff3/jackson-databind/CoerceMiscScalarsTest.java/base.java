package com.fasterxml.jackson.databind.convert;

import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.BaseMapTest;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.CoercionAction;
import com.fasterxml.jackson.databind.cfg.CoercionInputShape;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

public class CoerceMiscScalarsTest extends BaseMapTest
{
    private final ObjectMapper DEFAULT_MAPPER = sharedMapper();

    private final ObjectMapper MAPPER_EMPTY_TO_FAIL;
    {
        MAPPER_EMPTY_TO_FAIL = newJsonMapper();
        MAPPER_EMPTY_TO_FAIL.coercionConfigDefaults()
            .setCoercion(CoercionInputShape.EmptyString, CoercionAction.Fail);
    }

    private final ObjectMapper MAPPER_EMPTY_TO_EMPTY;
    {
        MAPPER_EMPTY_TO_EMPTY = newJsonMapper();
        MAPPER_EMPTY_TO_EMPTY.coercionConfigDefaults()
            .setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsEmpty);
    }

    private final ObjectMapper MAPPER_EMPTY_TO_TRY_CONVERT;
    {
        MAPPER_EMPTY_TO_TRY_CONVERT = newJsonMapper();
        MAPPER_EMPTY_TO_TRY_CONVERT.coercionConfigDefaults()
            .setCoercion(CoercionInputShape.EmptyString, CoercionAction.TryConvert);
    }
    
    private final ObjectMapper MAPPER_EMPTY_TO_NULL;
    {
        MAPPER_EMPTY_TO_NULL = newJsonMapper();
        MAPPER_EMPTY_TO_NULL.coercionConfigDefaults()
            .setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsNull);
    }

    private final String JSON_EMPTY = quote("");

    /*
    /********************************************************
    /* Test methods, defaults (legacy)
    /********************************************************
     */

    public void testScalarDefaultsFromEmpty() throws Exception
    {
        // mostly as null, with some exceptions

        _testScalarEmptyToNull(DEFAULT_MAPPER, UUID.class);

        _testScalarEmptyToNull(DEFAULT_MAPPER, File.class);
        _testScalarEmptyToNull(DEFAULT_MAPPER, URL.class);

        _testScalarEmptyToEmpty(DEFAULT_MAPPER, URI.class,
                URI.create(""));

        _testScalarEmptyToNull(DEFAULT_MAPPER, Class.class);
        _testScalarEmptyToNull(DEFAULT_MAPPER, JavaType.class);
        _testScalarEmptyToNull(DEFAULT_MAPPER, Currency.class);
        _testScalarEmptyToNull(DEFAULT_MAPPER, Pattern.class);

        _testScalarEmptyToEmpty(DEFAULT_MAPPER, Locale.class,
                Locale.ROOT);

        _testScalarEmptyToNull(DEFAULT_MAPPER, Charset.class);
        _testScalarEmptyToNull(DEFAULT_MAPPER, TimeZone.class);
        _testScalarEmptyToNull(DEFAULT_MAPPER, InetAddress.class);
        _testScalarEmptyToNull(DEFAULT_MAPPER, InetSocketAddress.class);

        {
            StringBuilder result = DEFAULT_MAPPER.readValue(JSON_EMPTY, StringBuilder.class);
            assertNotNull(result);
            assertEquals(0, result.length());
        }
    }

    /*
    /********************************************************
    /* Test methods, successful coercions from empty String
    /********************************************************
     */

    public void testScalarEmptyToNull() throws Exception
    {
        _testScalarEmptyToNull(MAPPER_EMPTY_TO_NULL, UUID.class);

        _testScalarEmptyToNull(MAPPER_EMPTY_TO_NULL, File.class);
        _testScalarEmptyToNull(MAPPER_EMPTY_TO_NULL, URL.class);
        _testScalarEmptyToNull(MAPPER_EMPTY_TO_NULL, URI.class);
        _testScalarEmptyToNull(MAPPER_EMPTY_TO_NULL, Class.class);
        _testScalarEmptyToNull(MAPPER_EMPTY_TO_NULL, JavaType.class);
        _testScalarEmptyToNull(MAPPER_EMPTY_TO_NULL, Currency.class);
        _testScalarEmptyToNull(MAPPER_EMPTY_TO_NULL, Pattern.class);
        _testScalarEmptyToNull(MAPPER_EMPTY_TO_NULL, Locale.class);
        _testScalarEmptyToNull(MAPPER_EMPTY_TO_NULL, Charset.class);
        _testScalarEmptyToNull(MAPPER_EMPTY_TO_NULL, TimeZone.class);
        _testScalarEmptyToNull(MAPPER_EMPTY_TO_NULL, InetAddress.class);
        _testScalarEmptyToNull(MAPPER_EMPTY_TO_NULL, InetSocketAddress.class);

        _testScalarEmptyToNull(MAPPER_EMPTY_TO_NULL, StringBuilder.class);
    }

    public void testScalarEmptyToEmpty() throws Exception
    {
        _testScalarEmptyToEmpty(MAPPER_EMPTY_TO_EMPTY, UUID.class,
                new UUID(0L, 0L));

        _testScalarEmptyToNull(MAPPER_EMPTY_TO_EMPTY, File.class);
        _testScalarEmptyToNull(MAPPER_EMPTY_TO_EMPTY, URL.class);
        
        _testScalarEmptyToEmpty(MAPPER_EMPTY_TO_EMPTY, URI.class,
                URI.create(""));

        _testScalarEmptyToNull(MAPPER_EMPTY_TO_EMPTY, Class.class);
        _testScalarEmptyToNull(MAPPER_EMPTY_TO_EMPTY, JavaType.class);
        _testScalarEmptyToNull(MAPPER_EMPTY_TO_EMPTY, Currency.class);
        _testScalarEmptyToNull(MAPPER_EMPTY_TO_EMPTY, Pattern.class);

        _testScalarEmptyToEmpty(MAPPER_EMPTY_TO_EMPTY, Locale.class,
                Locale.ROOT);

        _testScalarEmptyToNull(MAPPER_EMPTY_TO_EMPTY, Charset.class);
        _testScalarEmptyToNull(MAPPER_EMPTY_TO_EMPTY, TimeZone.class);
        _testScalarEmptyToNull(MAPPER_EMPTY_TO_EMPTY, InetAddress.class);
        _testScalarEmptyToNull(MAPPER_EMPTY_TO_EMPTY, InetSocketAddress.class);

        {
            StringBuilder result = MAPPER_EMPTY_TO_EMPTY.readValue(JSON_EMPTY, StringBuilder.class);
            assertNotNull(result);
            assertEquals(0, result.length());
        }
    }

    public void testScalarEmptyToTryConvert() throws Exception
    {
        // Should be same as `AsNull` for most but not all
        _testScalarEmptyToNull(MAPPER_EMPTY_TO_TRY_CONVERT, UUID.class);

        _testScalarEmptyToNull(MAPPER_EMPTY_TO_TRY_CONVERT, File.class);
        _testScalarEmptyToNull(MAPPER_EMPTY_TO_TRY_CONVERT, URL.class);

        _testScalarEmptyToEmpty(MAPPER_EMPTY_TO_TRY_CONVERT, URI.class,
                URI.create(""));

        _testScalarEmptyToNull(MAPPER_EMPTY_TO_TRY_CONVERT, Class.class);
        _testScalarEmptyToNull(MAPPER_EMPTY_TO_TRY_CONVERT, JavaType.class);
        _testScalarEmptyToNull(MAPPER_EMPTY_TO_TRY_CONVERT, Currency.class);
        _testScalarEmptyToNull(MAPPER_EMPTY_TO_TRY_CONVERT, Pattern.class);

        _testScalarEmptyToEmpty(MAPPER_EMPTY_TO_TRY_CONVERT, Locale.class,
                Locale.ROOT);

        _testScalarEmptyToNull(MAPPER_EMPTY_TO_TRY_CONVERT, Charset.class);
        _testScalarEmptyToNull(MAPPER_EMPTY_TO_TRY_CONVERT, TimeZone.class);
        _testScalarEmptyToNull(MAPPER_EMPTY_TO_TRY_CONVERT, InetAddress.class);
        _testScalarEmptyToNull(MAPPER_EMPTY_TO_TRY_CONVERT, InetSocketAddress.class);

        {
            StringBuilder result = MAPPER_EMPTY_TO_TRY_CONVERT.readValue(JSON_EMPTY, StringBuilder.class);
            assertNotNull(result);
            assertEquals(0, result.length());
        }
    }

    /*
    /********************************************************
    /* Test methods, failed coercions from empty String
    /********************************************************
     */

    public void testScalarsFailFromEmpty() throws Exception
    {
        _verifyScalarToFail(MAPPER_EMPTY_TO_FAIL, UUID.class);
    }

    /*
    /********************************************************
    /* Second-level test helper methods
    /********************************************************
     */
    
    private void _testScalarEmptyToNull(ObjectMapper mapper, Class<?> target) throws Exception
    {
        assertNull(mapper.readerFor(target).readValue(JSON_EMPTY));
    }

    private void _testScalarEmptyToEmpty(ObjectMapper mapper,
            Class<?> target, Object emptyValue) throws Exception
    {
        Object result = mapper.readerFor(target).readValue(JSON_EMPTY);
        assertEquals(emptyValue, result);
    }

    private void _verifyScalarToFail(ObjectMapper mapper, Class<?> target) throws Exception
    {
        try {
            /*Object result =*/ mapper.readerFor(target)
                .readValue(JSON_EMPTY);
            fail("Should not pass");
        } catch (MismatchedInputException e) {
            verifyException(e, "Cannot coerce empty String ");
            verifyException(e, " to `"+target.getName());
        }
    }
}
