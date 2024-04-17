import { Quote } from "@/types/quote";
import { BookOpenIcon, HeartIcon, PencilIcon, HashtagIcon, UserIcon } from "@heroicons/react/24/outline";

const SinglePendingQuote = ({ quote }: { quote: Quote }) => {
  const { content, book, likes } = quote;

  return (
    <div className="w-full mt-10">
      <div
        className="rounded-sm bg-white p-8 shadow-two duration-300 hover:shadow-one dark:bg-dark dark:shadow-three dark:hover:shadow-gray-dark lg:px-5 xl:px-8">
        <p
          className="mb-4 pb-4 border-b border-body-color border-opacity-10 leading-relaxed text-dark dark:border-white dark:border-opacity-10 text-lg dark:text-white lg:text-base xl:text-lg">
          {content}
        </p>
        <div className="flex items-center">
          <div className="w-full">
            <h3 className="text-dark dark:text-white text-base">
              <BookOpenIcon className="h-6 w-6 inline-block" /> {book.title}
            </h3>
            <div className="mt-5">
              <span><PencilIcon className="h-6 w-6 inline-block" /> </span>
              {book.authors.map((a, index) => (
                <span key={a.id} className="text-dark dark:text-white text-base">
                  {(index ? ", " : "") + a.name} {a.surname}{(a.patronymic ? ` ${a.patronymic}` : "")}</span>
              ))}
            </div>
            <div className="mt-5">
              <span><HashtagIcon className="h-6 w-6 inline-block" /> </span>
              {quote.tags.map((t, index) => (
                <span key={t.id} className="text-dark dark:text-white text-base">
                  {(index ? ", " : "") + t.name}</span>
              ))}
            </div>
            <div className="mt-5">
              <UserIcon className="h-6 w-6 inline-block" /> {quote.profile.login}
            </div>
            <div className="mt-5">
              <button
                className="bg-blue-500 hover:bg-blue-700 dark:bg-blue-700 dark:hover:bg-blue-900 text-white font-bold py-2 px-4 rounded-full text-base">
                <HeartIcon className="h-6 w-6 inline-block" /> {likes}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default SinglePendingQuote;
