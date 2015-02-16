package mobi.eyeline.ips.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

/**
 * Ответ на вопрос в случае, когда пользователь ввёл произвольный текст вместо номераварианта ответа
 */
@Entity
@DiscriminatorValue("text_answer")
public class TextAnswer extends Answer {
    /**
     * Текст ответа на вопрос.
     */
    //TODO: any additional restrictions here?
    @Column(name = "answer_text")
    @NotNull
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
