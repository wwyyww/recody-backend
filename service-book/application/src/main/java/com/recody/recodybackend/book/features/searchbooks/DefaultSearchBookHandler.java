package com.recody.recodybackend.book.features.searchbooks;


import com.recody.recodybackend.book.Book;
import com.recody.recodybackend.book.data.book.BookEntity;
import com.recody.recodybackend.book.data.book.BookMapper;
import com.recody.recodybackend.book.data.book.BookRepository;
import com.recody.recodybackend.book.features.event.BookEventPublisher;
import com.recody.recodybackend.book.features.event.BookQueried;
import com.recody.recodybackend.book.general.Books;
import com.recody.recodybackend.common.data.QueryMetadata;
import com.recody.recodybackend.common.utils.LanguageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
class DefaultSearchBookHandler implements SearchBookHandler<Books>{

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookEventPublisher bookEventPublisher;

    @Override
    public Books handle(SearchBooks query) {
        log.debug( "handling query: {}", query.getKeyword() );
        Locale language = LanguageUtils.languageOf(query.getKeyword());
//        List<BookEntity> bookEntities = bookRepository.findByTitleLike(query.getKeyword(), Pageable.unpaged());
        Page<BookEntity> bookEntitiesPage = bookRepository.findPagedByTitleLike(query.getKeyword(),
                PageRequest.of(query.getPage() - 1, 10));
        bookEventPublisher.publish(BookQueried.builder()
                .keyword(query.getKeyword())
                .build());
        QueryMetadata queryMetadata = new QueryMetadata(bookEntitiesPage);
        log.debug( "{} 개의 결과를 검색하였습니다.", queryMetadata.getSize() );
        return Books.of(bookMapper.map(bookEntitiesPage.getContent(), query.getLocale()), queryMetadata);

//        return Books.of(bookMapper.map(bookEntities, query.getLocale()));
    }



}
