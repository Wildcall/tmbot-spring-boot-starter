package ru.malygin.tmbot;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodSerializable;
import org.telegram.telegrambots.meta.api.methods.send.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ReplyPayload {

    private BotApiMethod<?> message;
    private final List<BotApiMethodSerializable> botApiMethodSerializableList = new ArrayList<>();
    private final List<SendPhoto> sendPhotoList = new ArrayList<>();
    private final List<SendDocument> sendDocumentList = new ArrayList<>();
    private final List<SendVideo> sendVideoList = new ArrayList<>();
    private final List<SendVideoNote> sendVideoNoteList = new ArrayList<>();
    private final List<SendSticker> sendStickerList = new ArrayList<>();
    private final List<SendAudio> sendAudioList = new ArrayList<>();
    private final List<SendVoice> sendVoiceList = new ArrayList<>();
    private final List<SendAnimation> sendAnimationList = new ArrayList<>();

    public ReplyPayload(BotApiMethod<?> message) {
        this.message = message;
    }

    public void addPayload(BotApiMethodSerializable serializable) {
        this.botApiMethodSerializableList.add(serializable);
    }

    public void addPayload(SendPhoto payload) {
        this.sendPhotoList.add(payload);
    }

    public void addPayload(SendDocument payload) {
        this.sendDocumentList.add(payload);
    }

    public void addPayload(SendVideo payload) {
        this.sendVideoList.add(payload);
    }

    public void addPayload(SendVideoNote payload) {
        this.sendVideoNoteList.add(payload);
    }

    public void addPayload(SendSticker payload) {
        this.sendStickerList.add(payload);
    }

    public void addPayload(SendAudio payload) {
        this.sendAudioList.add(payload);
    }

    public void addPayload(SendVoice payload) {
        this.sendVoiceList.add(payload);
    }

    public void addPayload(SendAnimation payload) {
        this.sendAnimationList.add(payload);
    }
}
