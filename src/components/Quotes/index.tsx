import { Quote } from "@/types/quote";
import SingleQuote from "./SingleQuote";

const quoteData: Quote[] = [
  {
    id: 1,
    content: "Правильного выбора в реальности не существует — есть только сделанный выбор и его последствия.",
    book: {
      id: 1,
      title: "Мне тебя обещали",
      authors: [
        {
          id: 1,
          surname: "Сафарли",
          name: "Эльчин",
          patronymic: null
        }
      ]
    },
    profile:{
      id: 1,
      email: "string@string.ru",
      login: "sergeylavrov",
    },
    status: {
      id: 3,
      name: "PUBLIC"
    },
    likes: 3,
    tags: [
      {
        id: 1,
        name: "выбор"
      },
      {
        id: 1,
        name: "последствия"
      },
    ]
  },

  {
    id: 2,
    content: "Разговоры... Странная это все таки вещь. Можно обменяться миллионом слов и... не сказать главного. " +
      "А можно молча смотреть в глаза и... поведать обо всем.",
    book: {
      id: 2,
      title: "Тяжело быть младшим",
      authors: [
        {
          id: 2,
          surname: "Баштовая",
          name: "Ксения",
          patronymic: null
        },
        {
          id: 3,
          surname: "Иванова",
          name: "Виктория",
          patronymic: null
        }
      ]
    },
    profile:{
      id: 2,
      email: "test@test.ru",
      login: "testuser",
    },
    status: {
      id: 3,
      name: "PUBLIC"
    },
    likes: 1,
    tags: [
      {
        id: 1,
        name: "разговор"
      },
      {
        id: 1,
        name: "общение"
      },
    ],
  }
];

const Quotes = () => {
  return (
    <section className="dark:bg-bg-color-dark bg-gray-light relative z-10 py-16 md:py-20 lg:py-28">
      <div className="container">
        <div className="grid grid-cols-1 gap-x-8 gap-y-10 md:grid-cols-2 lg:grid-cols-3">
          {quoteData.map((q) => (
            <SingleQuote key={q.id} quote={q} />
          ))}
        </div>
      </div>
    </section>
  );
};

export default Quotes;
