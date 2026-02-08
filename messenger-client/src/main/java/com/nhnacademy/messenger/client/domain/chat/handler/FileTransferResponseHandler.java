package com.nhnacademy.messenger.client.domain.chat.handler;

import com.nhnacademy.messenger.client.network.ResponseHandler;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.data.file.FileTransferResponse;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileTransferResponseHandler implements ResponseHandler {

    @Override
    public void handle(Message message) {
        try {
            FileTransferResponse response = (FileTransferResponse) MessageConverter.toData(message);
            log.info("파일 업로드 성공 확인: roomId={}, fileName={}, messageId={}",
                    response.roomId(), response.fileName(), response.messageId());
        } catch (Exception e) {
            log.warn("파일 전송 응답 처리 중 오류 발생: {}", e.getMessage());
        }
    }
}
