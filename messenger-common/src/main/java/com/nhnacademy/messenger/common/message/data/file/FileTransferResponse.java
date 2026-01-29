package com.nhnacademy.messenger.common.message.data.file;

import com.nhnacademy.messenger.common.message.data.MessageData;

public record FileTransferResponse(
        Long roomId,
        Long messageId,
        String fileName
) implements MessageData {
}
