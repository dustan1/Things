#ifndef MESSAGE_QUEUE_CONSTS_H_INCLUDED
#define MESSAGE_QUEUE_CONSTS_H_INCLUDED

// From assignment recommendations:
// "Please ensure that each sentence transferred is no more than 35 characters in length."
#define MAX_TEXT_LENGTH 35

#define APPEND_E 1
#define DELETE_E 2
#define REMOVE_E 3
#define SEARCH_E 4

#define RECIEVE_QUEUE_ID 1236
#define SEND_QUEUE_ID 1237

struct my_msg_st {
   long int my_msg_type;
   char some_text[BUFSIZ];
};

#endif // MESSAGE_QUEUE_CONSTS_H_INCLUDED
