package mobi.eyeline.ips.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * Стандартный ответ на вопрос(пользователь выбрал вариант из предложенных)
 */
@Entity
@DiscriminatorValue("option_answer")
public class OptionAnswer extends Answer {

    /**
     * Вариант ответа на вопрос.
     */
    @JoinColumn(name = "option_id")
    @ManyToOne(optional = true)
    @NotNull(message = "{answer.validation.option.empty}")
    private QuestionOption option;

    public QuestionOption getOption() {
        return option;
    }

    public void setOption(QuestionOption option) {
        this.option = option;
    }
}
