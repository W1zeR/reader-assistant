import { Quote } from "@/types/quote";
import SinglePrivateQuote from "./SinglePrivateQuote";
import { PlusIcon } from "@heroicons/react/24/outline";
import Link from "next/link";

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
    profile: {
      id: 1,
      email: "string@string.ru",
      login: "sergeylavrov"
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
        id: 2,
        name: "последствия"
      }
    ]
  },

  {
    id: 2,
    content: "Каждый живет, как хочет, и расплачивается за это сам.",
    book: {
      id: 2,
      title: "Портрет Дориана Грея",
      authors: [
        {
          id: 2,
          surname: "Уайльд",
          name: "Оскар",
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
      id: 1,
      name: "PRIVATE"
    },
    likes: 1,
    tags: [
      {
        id: 1,
        name: "жизнь"
      }
    ]
  },

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

const PrivateQuotes = () => {
  return (
    <section className="dark:bg-bg-color-dark bg-gray-light relative z-10 py-16 md:py-20 lg:py-28">
      <div className="container">
        <div className="py-4 flex justify-center mt-10">
          <button
            className="bg-green-500 hover:bg-green-700 dark:bg-green-700 dark:hover:bg-green-900 text-white py-2 px-4 rounded-full text-base">
            <Link
              href="/new-quote">
              <PlusIcon className="h-6 w-6 inline-block" /> Добавить новую цитату
            </Link>
          </button>
        </div>
        <div className="grid grid-cols-1 gap-x-8 gap-y-10 md:grid-cols-2 lg:grid-cols-3">
          {quoteData.map((q) => (
            <SinglePrivateQuote key={q.id} quote={q} />
          ))}
        </div>
      </div>
    </section>
  );
};

export default PrivateQuotes;
