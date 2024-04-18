import { Quote } from "@/types/quote";
import SinglePendingQuote from "./SinglePendingQuote";

const quoteData: Quote[] = [
  {
    id: 3,
    content: "Проще расстаться с человеком, чем с иллюзиями на его счёт.",
    book: {
      id: 2,
      title: "Улыбайся всегда, любовь моя",
      authors: [
        {
          id: 2,
          surname: "Кетро",
          name: "Марта",
          patronymic: null
        }
      ]
    },
    profile: {
      id: 1,
      email: "string@string.ru",
      login: "sergeylavrov"
    },
    status: {
      id: 2,
      name: "PENDING"
    },
    likes: 1,
    tags: [
      {
        id: 1,
        name: "расставание"
      },
      {
        id: 2,
        name: "отношения"
      },
      {
        id: 3,
        name: "иллюзии"
      }
    ]
  }
];

const PendingQuotes = () => {
  return (
    <section className="dark:bg-bg-color-dark bg-gray-light relative z-10 py-16 md:py-20 lg:py-28">
      <div className="container">
        <div className="grid grid-cols-1 gap-x-8 gap-y-10 md:grid-cols-2 lg:grid-cols-3">
          {quoteData.map((q) => (
            <SinglePendingQuote key={q.id} quote={q} />
          ))}
        </div>
      </div>
    </section>
  );
};

export default PendingQuotes;
