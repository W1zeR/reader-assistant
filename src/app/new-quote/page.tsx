import { BookOpenIcon, PencilIcon, HashtagIcon } from "@heroicons/react/24/outline";

const NewQuote = () => {
  return (
    <>
      <section className="relative z-10 overflow-hidden pb-16 pt-36 md:pb-20 lg:pb-28 lg:pt-[180px]">
        <div className="container">
          <div className="-mx-4 flex flex-nowrap">
            <div className="w-full px-4">
              <div className="shadow-three mx-auto max-w-[500px] rounded bg-white px-6 py-10 dark:bg-dark sm:p-[60px]">
                <h3 className="mb-3 text-center text-2xl font-bold text-black dark:text-white sm:text-3xl">
                  Добавить цитату
                </h3>
                <div className="mb-8 flex items-center justify-center" />
                <form>
                  <div className="mb-8">
                    <label
                      htmlFor="content"
                      className="mb-3 block text-sm font-medium text-dark dark:text-white"
                    >
                      Содержание
                    </label>
                    <textarea
                      name="content"
                      rows={5}
                      placeholder="Введите текст цитаты"
                      className="border-stroke w-full resize-none rounded-sm border bg-[#f8f8f8] px-6 py-3 text-base text-body-color outline-none focus:border-primary dark:border-transparent dark:bg-[#2C303B] dark:text-body-color-dark dark:shadow-two dark:focus:border-primary dark:focus:shadow-none"
                    ></textarea>
                  </div>
                  <div className="mb-8">
                    <label
                      htmlFor="book"
                      className="mb-3 block text-sm text-dark dark:text-white"
                    >
                      {" "}
                      <BookOpenIcon className="h-6 w-6 inline-block" /> Книга{" "}
                    </label>
                    <input
                      type="text"
                      name="book"
                      placeholder="Введите название книги"
                      className="border-stroke dark:text-body-color-dark dark:shadow-two w-full rounded-sm border bg-[#f8f8f8] px-6 py-3 text-base text-body-color outline-none transition-all duration-300 focus:border-primary dark:border-transparent dark:bg-[#2C303B] dark:focus:border-primary dark:focus:shadow-none"
                    />
                  </div>
                  <div className="mb-8">
                    <label
                      htmlFor="authors"
                      className="mb-3 block text-sm text-dark dark:text-white"
                    >
                      {" "}
                      <PencilIcon className="h-6 w-6 inline-block" /> Авторы{" "}
                    </label>
                    <input
                      type="text"
                      name="authors"
                      placeholder="Введите авторов"
                      className="border-stroke dark:text-body-color-dark dark:shadow-two w-full rounded-sm border bg-[#f8f8f8] px-6 py-3 text-base text-body-color outline-none transition-all duration-300 focus:border-primary dark:border-transparent dark:bg-[#2C303B] dark:focus:border-primary dark:focus:shadow-none"
                    />
                  </div>
                  <div className="mb-8">
                    <label
                      htmlFor="tags"
                      className="mb-3 block text-sm text-dark dark:text-white"
                    >
                      {" "}
                      <HashtagIcon className="h-6 w-6 inline-block" /> Теги{" "}
                    </label>
                    <input
                      type="text"
                      name="tags"
                      placeholder="Введите теги"
                      className="border-stroke dark:text-body-color-dark dark:shadow-two w-full rounded-sm border bg-[#f8f8f8] px-6 py-3 text-base text-body-color outline-none transition-all duration-300 focus:border-primary dark:border-transparent dark:bg-[#2C303B] dark:focus:border-primary dark:focus:shadow-none"
                    />
                  </div>
                  <div className="mb-6">
                    <button
                      className="shadow-submit dark:shadow-submit-dark flex w-full items-center justify-center rounded-sm bg-primary px-9 py-4 text-base font-medium text-white duration-300 hover:bg-primary/90">
                      Добавить
                    </button>
                  </div>
                  <div className="mb-6">
                    <button
                      className="shadow-submit dark:shadow-submit-dark flex w-full items-center justify-center rounded-sm bg-red-700 px-9 py-4 text-base font-medium text-white duration-300 hover:bg-red-700/90">
                      Отмена
                    </button>
                  </div>
                </form>
              </div>
            </div>
          </div>
        </div>
      </section>
    </>
  );
};

export default NewQuote;