package com.recody.recodybackend.drama.tmdb;

import com.recody.recodybackend.common.exceptions.ApplicationExceptions;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class TMDBPersonName {
    
    @Getter
    private final String value;
    
    private TMDBPersonName(String value) {
        ApplicationExceptions.requireNonNull( value );
        this.value = value;
    }
    
    public static TMDBPersonName of(String value) {
        return new TMDBPersonName( value );
    }
    
    public static TMDBPersonName firstKoreanNameOf(List<String> names) {
        String foundName = "";
        if ( Objects.isNull( names ) ) {
            return new TMDBPersonName( foundName );
        }
        for (String name : names) {
            // 한글, 공백으로 구성된 문자열을 찾는다.
            if ( Pattern.matches( "^[가-힣\\s]+$", name ) ) {
                foundName = name;
                break;
            }
        }
        return new TMDBPersonName( foundName );
    }
    
    public static TMDBPersonName firstKoreanNameOf(List<String> names, String defaultName) {
        TMDBPersonName tmdbPersonName = firstKoreanNameOf( names );
        return hasText( tmdbPersonName ) ? tmdbPersonName : TMDBPersonName.of( defaultName );
    }
    
    public static TMDBPersonName firstEnglishNameOf(List<String> names) {
        String foundName = "";
        if ( Objects.isNull( names ) ) {
            return new TMDBPersonName( foundName );
        }
        for (String name : names) {
            // 한글, 공백으로 구성된 문자열을 찾는다.
            if ( Pattern.matches( "^[a-zA-Z\\s]+$", name ) ) {
                foundName = name;
                break;
            }
        }
        return new TMDBPersonName( foundName );
    }
    
    public static TMDBPersonName firstEnglishNameOf(List<String> names, String defaultName) {
        TMDBPersonName tmdbPersonName = TMDBPersonName.firstEnglishNameOf( names );
        return hasText( tmdbPersonName ) ? tmdbPersonName : TMDBPersonName.of( defaultName );
    }
    
    
    private static boolean hasText(TMDBPersonName tmdbPersonName) {
        return StringUtils.hasText( tmdbPersonName.getValue() );
    }
    
    @Override
    public String toString() {
        return "{\"TMDBPersonName\":{"
               + "\"value\":" + ((value != null) ? ("\"" + value + "\"") : null)
               + "}}";
    }
}
