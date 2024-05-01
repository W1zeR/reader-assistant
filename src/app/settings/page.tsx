import { Metadata } from "next";

export const metadata: Metadata = {
  title: "Помощник читателя | Настройки",
  description: "Это страница с настройками аккаунта"
};

const Settings = () => {
  return (
    <>
      <section className="relative z-10 overflow-hidden pb-16 pt-36 md:pb-20 lg:pb-28 lg:pt-[180px]">
        <div className="container">
          <div className="-mx-4 flex flex-nowrap">
            <div className="w-full px-4">
              <div className="shadow-three mx-auto max-w-[500px] rounded bg-white px-6 py-10 dark:bg-dark sm:p-[60px]">
                <h3 className="mb-3 text-center text-2xl font-bold text-black dark:text-white sm:text-3xl">
                  Настройки аккаунта
                </h3>
                <div className="mb-8 flex items-center justify-center" />
                <form>
                  <div className="mb-8">
                    <label
                      htmlFor="email"
                      className="mb-3 block text-sm text-dark dark:text-white"
                    >
                      {" "}
                      Адрес электронной почты{" "}
                    </label>
                    <input
                      type="email"
                      name="email"
                      placeholder=""
                      className="border-stroke dark:text-body-color-dark dark:shadow-two w-full rounded-sm border
                      bg-[#f8f8f8] px-6 py-3 text-base text-body-color outline-none transition-all duration-300
                      focus:border-primary dark:border-transparent dark:bg-[#2C303B] dark:focus:border-primary
                      dark:focus:shadow-none"
                    />
                  </div>
                  <div className="mb-8">
                    <label
                      htmlFor="login"
                      className="mb-3 block text-sm text-dark dark:text-white"
                    >
                      {" "}
                      Имя пользователя{" "}
                    </label>
                    <input
                      type="text"
                      name="login"
                      placeholder=""
                      className="border-stroke dark:text-body-color-dark dark:shadow-two w-full rounded-sm border
                      bg-[#f8f8f8] px-6 py-3 text-base text-body-color outline-none transition-all duration-300
                      focus:border-primary dark:border-transparent dark:bg-[#2C303B] dark:focus:border-primary
                      dark:focus:shadow-none"
                    />
                  </div>
                </form>
                <form>
                  <div className="mb-8">
                    <label
                      htmlFor="currentPassword"
                      className="mb-3 block text-sm text-dark dark:text-white"
                    >
                      {" "}
                      Текущий пароль{" "}
                    </label>
                    <input
                      type="password"
                      name="currentPassword"
                      placeholder="Введите текущий пароль"
                      className="border-stroke dark:text-body-color-dark dark:shadow-two w-full rounded-sm border
                      bg-[#f8f8f8] px-6 py-3 text-base text-body-color outline-none transition-all duration-300
                      focus:border-primary dark:border-transparent dark:bg-[#2C303B] dark:focus:border-primary
                      dark:focus:shadow-none"
                    />
                  </div>
                  <div className="mb-8">
                    <label
                      htmlFor="newPassword"
                      className="mb-3 block text-sm text-dark dark:text-white"
                    >
                      {" "}
                      Новый пароль{" "}
                    </label>
                    <input
                      type="password"
                      name="newPassword"
                      placeholder="Введите новый пароль"
                      className="border-stroke dark:text-body-color-dark dark:shadow-two w-full rounded-sm border
                      bg-[#f8f8f8] px-6 py-3 text-base text-body-color outline-none transition-all duration-300
                      focus:border-primary dark:border-transparent dark:bg-[#2C303B] dark:focus:border-primary
                      dark:focus:shadow-none"
                    />
                  </div>
                  <div className="mb-8">
                    <label
                      htmlFor="confirmNewPassword"
                      className="mb-3 block text-sm text-dark dark:text-white"
                    >
                      {" "}
                      Подтвердите новый пароль{" "}
                    </label>
                    <input
                      type="password"
                      name="confirmNewPassword"
                      placeholder="Повторно введите новый пароль"
                      className="border-stroke dark:text-body-color-dark dark:shadow-two w-full rounded-sm border
                      bg-[#f8f8f8] px-6 py-3 text-base text-body-color outline-none transition-all duration-300
                      focus:border-primary dark:border-transparent dark:bg-[#2C303B] dark:focus:border-primary
                      dark:focus:shadow-none"
                    />
                  </div>
                </form>
                <div className="mb-6">
                  <button
                    className="shadow-submit dark:shadow-submit-dark flex w-full items-center justify-center
                    rounded-sm bg-primary px-9 py-4 text-base font-medium text-white duration-300 hover:bg-primary/90">
                    Сохранить
                  </button>
                </div>
                <div className="mb-6">
                  <button
                    className="shadow-submit dark:shadow-submit-dark flex w-full items-center justify-center
                    rounded-sm bg-red-700 px-9 py-4 text-base font-medium text-white duration-300 hover:bg-red-700/90">
                    Отмена
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
    </>
  );
};

export default Settings;
