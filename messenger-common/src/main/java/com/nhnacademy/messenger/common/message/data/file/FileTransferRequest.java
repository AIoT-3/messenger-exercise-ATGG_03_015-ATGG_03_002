package com.nhnacademy.messenger.common.message.data.file;

import com.nhnacademy.messenger.common.message.data.MessageData;

public record FileTransferRequest (
        Long roomId,
        String fileName,
        Long fileSize,
        String fileData
) implements MessageData {
}
