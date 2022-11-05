package com.recody.recodybackend.record.web;

import com.recody.recodybackend.common.web.SuccessResponseBody;
import com.recody.recodybackend.commonbootutils.jwt.JwtManager;
import com.recody.recodybackend.commonbootutils.web.AccessToken;
import com.recody.recodybackend.record.features.RecordService;
import com.recody.recodybackend.record.features.addrecord.AddRecord;
import com.recody.recodybackend.record.features.completerecord.CompleteRecord;
import com.recody.recodybackend.record.features.continuerecord.ContinueRecord;
import com.recody.recodybackend.record.features.deleterecord.DeleteRecord;
import com.recody.recodybackend.record.features.getcontinuingrecord.GetContinuingRecord;
import com.recody.recodybackend.record.features.getmyrecords.GetMyRecords;
import com.recody.recodybackend.record.features.getrecord.GetRecord;
import com.recody.recodybackend.record.features.getrecordcontents.GetRecordContents;
import com.recody.recodybackend.record.features.resolvecategory.CategoryResolver;
import com.recody.recodybackend.record.features.totalrecords.CountTotalRecords;
import com.recody.recodybackend.record.features.totalrecords.CountTotalRecordsHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class RecordController {
    
    private final MessageSource ms;
    private final RecordService recordService;
    private final JwtManager jwtManager;
    private final CategoryResolver categoryResolver;
    
    @PostMapping( "/api/v1/record/complete" )
    public ResponseEntity<SuccessResponseBody> complete(HttpServletRequest httpServletRequest,
                                                        @Valid @RequestBody CompleteRecordRequest request,
                                                        @AccessToken String accessToken) {
        return ResponseEntity.ok( SuccessResponseBody
                                          .builder()
                                          .message( ms.getMessage( "record.complete.succeeded", null,
                                                                   httpServletRequest.getLocale() ) )
                                          .data( recordService.completeRecord( CompleteRecord
                                                                                       .builder()
                                                                                       .recordId( request.getRecordId() )
                                                                                       .title( request.getTitle() )
                                                                                       .note( request.getNote() )
                                                                                       .build() ) )
                                          .build() );
    }
    
    @GetMapping( "/api/v1/record/continuing" )
    public ResponseEntity<SuccessResponseBody> getContinuingRecord(HttpServletRequest httpServletRequest,
                                                                   @AccessToken String accessToken) {
        return ResponseEntity.ok( SuccessResponseBody
                                          .builder()
                                          .message( ms.getMessage( "record.get-continuing.succeeded", null,
                                                                   httpServletRequest.getLocale() ) )
                                          .data( recordService.getContinuingRecord( GetContinuingRecord
                                                                                            .builder()
                                                                                            .userId( jwtManager.resolveUserId(
                                                                                                    accessToken ) )
                                                                                            .build() ) )
                                          .build() );
    }
    
    @GetMapping( "/api/v1/record/{recordId}" )
    public ResponseEntity<SuccessResponseBody> getRecord(HttpServletRequest httpServletRequest,
                                                         @PathVariable String recordId) {
        return ResponseEntity.ok( SuccessResponseBody
                                          .builder()
                                          .message( ms.getMessage( "record.get.succeeded", null,
                                                                   httpServletRequest.getLocale() ) )
                                          .data( recordService.getRecord( GetRecord.builder().recordId( recordId ).build() ) )
                                          .build() );
    }
    
    @GetMapping( "/api/v1/record/content/{recordId}" )
    public ResponseEntity<SuccessResponseBody> getRecordContent(HttpServletRequest httpServletRequest,
                                                                @PathVariable String recordId) {
        return ResponseEntity.ok( SuccessResponseBody
                                          .builder()
                                          .message( ms.getMessage( "record.get-content.succeeded", null,
                                                                   httpServletRequest.getLocale() ) )
                                          .data( recordService.getRecord( GetRecord.builder().recordId( recordId ).build() ) )
                                          .build() );
    }
    
    @GetMapping( "/api/v1/record/records" )
    public ResponseEntity<SuccessResponseBody> getRecords(HttpServletRequest httpServletRequest,
                                                          @Nullable @RequestParam( defaultValue = "0" ) Integer page,
                                                          @Nullable @RequestParam( defaultValue = "10" ) Integer size,
                                                          @Nullable @RequestParam String categoryId,
                                                          @Nullable @RequestParam String contentId,
                                                          @AccessToken String accessToken) {
        return ResponseEntity.ok( SuccessResponseBody
                                          .builder()
                                          .message( ms.getMessage( "record.records.get.succeeded", null,
                                                                   httpServletRequest.getLocale() ) )
                                          .data( recordService.getRecords( GetMyRecords
                                                                                   .builder()
                                                                                   .userId( jwtManager.resolveUserId(
                                                                                           accessToken ) )
                                                                                   .page( page )
                                                                                   .size( size )
                                                                                   .category( categoryResolver.resolve( categoryId ) )
                                                                                   .contentId( contentId )
                                                                                   .build() ) )
                                          .build() );
    }
    
    @GetMapping( "/api/v1/record/contents" )
    public ResponseEntity<SuccessResponseBody> getRecordContents(HttpServletRequest httpServletRequest,
                                                                 @Nullable @RequestParam( defaultValue = "0" ) Integer page,
                                                                 @Nullable @RequestParam( defaultValue = "10" ) Integer size,
                                                                 @Nullable @RequestParam String categoryId,
                                                                 @AccessToken String accessToken) {
        return ResponseEntity.ok(
                SuccessResponseBody
                        .builder()
                        .message( ms.getMessage( "record.contents.get.succeeded", null,
                                                 httpServletRequest.getLocale() ) )
                        .data( recordService.getRecordContents(
                                GetRecordContents.builder()
                                                 .page( page )
                                                 .size( size )
                                                 .userId( jwtManager.resolveUserId( accessToken ) )
                                                 .locale( httpServletRequest.getLocale() )
                                                 .build() )
                        
                             )
                        .build() );
    }
    
    @GetMapping( "/api/v1/record/contents/continuing" )
    public ResponseEntity<SuccessResponseBody> getRecordContentsContinuing(HttpServletRequest httpServletRequest,
                                                                           @Nullable @RequestParam( defaultValue = "0" ) Integer page,
                                                                           @Nullable @RequestParam( defaultValue = "10" ) Integer size,
                                                                           @AccessToken String accessToken) {
        return ResponseEntity.ok(
                SuccessResponseBody
                        .builder()
                        .message( ms.getMessage( "record.contents.continuing.get.succeeded", null,
                                                 httpServletRequest.getLocale() ) )
                        .data( recordService.getRecordContents(
                                GetRecordContents.builder()
                                                 .page( page )
                                                 .size( size )
                                                 .completed( false )
                                                 .userId( jwtManager.resolveUserId( accessToken ) )
                                                 .locale( httpServletRequest.getLocale() )
                                                 .build() )
                        
                             )
                        .build() );
    }
    
    @PostMapping( "/api/v1/record" )
    public ResponseEntity<SuccessResponseBody> postRecord(HttpServletRequest httpServletRequest,
                                                          @Valid @RequestBody AddRecordRequest request,
                                                          @AccessToken String accessToken) {
        
        return ResponseEntity
                       .ok( SuccessResponseBody
                                    .builder()
                                    .message( ms.getMessage( "record.add.succeeded", null,
                                                             httpServletRequest.getLocale() ) )
                                    .data( recordService.addRecord( createAddRecordCommand( request, accessToken ) ) )
                                    .build() );
    }
    
    @PutMapping( "/api/v1/record" )
    public ResponseEntity<SuccessResponseBody> updateRecord(HttpServletRequest httpServletRequest,
                                                            @Valid @RequestBody ContinueRecordRequest request) {
        return ResponseEntity.ok( SuccessResponseBody
                                          .builder()
                                          .message( ms.getMessage( "record.continue.succeeded", null,
                                                                   httpServletRequest.getLocale() ) )
                                          .data( recordService.continueRecord( ContinueRecord
                                                                                       .builder()
                                                                                       .recordId( request.getRecordId() )
                                                                                       .note( request.getNote() )
                                                                                       .title( request.getTitle() )
                                                                                       .build() ) )
                                          .build() );
    }
    
    @DeleteMapping( "/api/v1/record/{recordId}" )
    public ResponseEntity<SuccessResponseBody> deleteRecord(HttpServletRequest httpServletRequest,
                                                            @PathVariable String recordId,
                                                            @AccessToken String accessToken) {
        return ResponseEntity.ok(
                SuccessResponseBody.builder()
                                   .message( ms.getMessage( "record.delete.succeeded", null, httpServletRequest.getLocale() ) )
                                   .data( recordService.deleteRecord( DeleteRecord.builder()
                                                                                  .recordId( recordId )
                                                                                  .userId( jwtManager.resolveUserId( accessToken ) )
                
                                                                                  .build() ) )
                                   .build() );
    }
    
    @GetMapping( "/api/v1/record/records/total" )
    public ResponseEntity<SuccessResponseBody> countRecords(HttpServletRequest httpServletRequest,
                                                            @RequestParam(defaultValue = "false") Boolean thisMonth,
                                                            @AccessToken String accessToken) {
        return ResponseEntity.ok(
                SuccessResponseBody.builder()
                                   .message( ms.getMessage( "record.records.count.total.succeeded", null, httpServletRequest.getLocale() ) )
                                   .data( recordService.countRecords(
                                           CountTotalRecords.builder()
                                                            .userId( jwtManager.resolveUserId( accessToken ) )
                                                            .thisMonth( thisMonth )
                                                            .build() ) )
                                   .build() );
    }
    
    private AddRecord createAddRecordCommand(AddRecordRequest request, String accessToken) {
        return AddRecord
                       .builder()
                       .contentId( request.getContentId() )
                       .title( request.getTitle() )
                       .note( request.getNote() )
                       .userId( jwtManager.resolveUserId( accessToken ) )
                       .appreciationDate( request.getAppreciationDate() )
                       .build();
    }
    
}
