# IPS

*IPS is a service where you can create a survey and see its statistics. The survey is provided to an interviewee by means of [MiniApps](https://dev.miniapps.run) or can be published directly via USSD/SMS or Telegram. See our [wiki](https://docs.miniapps.pro/display/MINIAPPS/How+to+connect+IPS+survey) for how it works.*

IPS is a polling/quiz system. Poll is built as a sequence of related questions. A polling is conducted in a pre-defined time period, that is it has the beginning and end time. Questions are divided into two groups:
- Questions with preset answers to choose from;
- Questions with indeterminate questions.

## Distribution Channel
Distribution channel is a method of notification of potential poll/quiz participants about the poll. IPS supports the following distribution channels:
- User base. The list of users created based on the operator’s database and the users are informed by receiving text, or multimedia messages. Mass delivery is made through systems that IPS has no connection with. For instance, operator’s SMS mass informer can be used.
- Balance check. Notification is placed in USSD messages that user receive when making balance enquiry with the operator. MAdv USSD advertising system is used for this distribution channel.

## Restrictions

As questions are delivered to participants over USSD, certain restrictions are put on text length:
- The question is invalid if it contains only ASCII symbols and their number is greater than 150;
- The question is invalid if it contains at least one non-ASCII symbol and the total number of symbols is greater than 75.
